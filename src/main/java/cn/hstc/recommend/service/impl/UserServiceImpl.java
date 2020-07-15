package cn.hstc.recommend.service.impl;

import cn.hstc.recommend.dao.MenuDao;
import cn.hstc.recommend.dao.UserRoleDao;
import cn.hstc.recommend.entity.UserRoleEntity;
import cn.hstc.recommend.exception.RRException;
import cn.hstc.recommend.service.UserRoleService;
import cn.hstc.recommend.utils.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hstc.recommend.dao.UserDao;
import cn.hstc.recommend.entity.UserEntity;
import cn.hstc.recommend.service.UserService;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {



    private TokenHelp tokenHelp = new TokenHelp();

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );
        Set<Integer> menuIdsSet = new LinkedHashSet<>();
        for(UserEntity userEntity : page.getRecords()){
            List<Integer> menuIdList = this.getAllPermsIds(userEntity.getId());
            for(Integer menuId : menuIdList){
                menuIdsSet.add(menuId);
            }
            userEntity.setMenuIds(menuIdsSet);
            userEntity.setRoleIds(userRoleService.getRoleIdsByUserId(userEntity.getId()));
            userEntity.setRoleNames(userRoleService.getRoleNamesByUserId(userEntity.getId()));
        }

        return new PageUtils(page);
    }

    @Override
    public Result loginValidate(String userName, String password) {
        //查找是否存在该用户名
        UserEntity userEntity = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                .eq("user_name", userName));
        //若不存在，提示用户登录失败
        if (null == userEntity) {
            return new Result().error("该账号未注册，请进行注册！");
        }

        //我的密码是使用uuid作为盐值加密的，所以这里登陆时候还需要做一次对比
        SimpleHash simpleHash = new SimpleHash("MD5", password,  userEntity.getSalt(),
                Constant.HASHINTERATIONS);
        if(!simpleHash.toHex().equals(userEntity.getPassword())){
            return new Result().error("密码不正确");
        }
        String token = tokenHelp.getToken(userEntity);
        Result result = new Result<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        map.put("user", userEntity);
        return result.ok(map);
    }

    @Override
    public UserEntity getById(Integer userId) {
        UserEntity userEntity = super.getById(userId);
        userEntity.setMenuIds(this.getAllPermsIds(userId));
        userEntity.setRoleIds(userRoleService.getRoleIdsByUserId(userEntity.getId()));
        userEntity.setRoleNames(userRoleService.getRoleNamesByUserId(userEntity.getId()));
        return userEntity;
    }

    @Override
    @Transactional(rollbackFor = RRException.class)
    public boolean updateById(UserEntity userEntity) {
        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        for(Integer roleId : userEntity.getRoleIds()){
            if(roleId != null){
                UserRoleEntity userRoleEntity = new UserRoleEntity();
                userRoleEntity.setUserId(userEntity.getId());
                userRoleEntity.setRoleId(roleId);
                userRoleEntities.add(userRoleEntity);
                userRoleService.remove(new QueryWrapper<UserRoleEntity>()
                    .eq("user_id",userEntity.getId())
                );
            }
        }
        userRoleService.saveBatch(userRoleEntities);
//        if(userRoleEntities.size() != 0){
//            userRoleService.saveOrUpdateBatch(userRoleEntities);
//        }
        userEntity.setUpdateTime(new Date());
        String result = this.REXValidate(userEntity);
        if (result != null) {
            throw new RRException(result);
        }
        //获取sha256加密后的user
        if(userEntity.getPassword() != null){
            userEntity  = ShiroUtils.getShiroUser(userEntity);
        }
        return this.retBool(this.baseMapper.updateById(userEntity));
    }

    @Override
    public boolean save(UserEntity userEntity) {
        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        for(Integer roleId : userEntity.getRoleIds()){
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getId());
            userRoleEntity.setRoleId(roleId);
        }
        userRoleService.saveOrUpdateBatch(userRoleEntities);
        //设置创建时间
        userEntity.setCreateTime(new Date());
        //验证格式
        String result = this.REXValidate(userEntity);

        if (result != null) {
            throw new RRException(result);
        }
        //获取sha256加密后的user
        UserEntity shiroUser = ShiroUtils.getShiroUser(userEntity);


        return this.retBool(this.baseMapper.insert(shiroUser));
    }

    private String REXValidate(UserEntity userEntity) {



        if (null != userEntity.getUserName()) {
            String username = userEntity.getUserName();
            if (!username.matches(Constant.USERNAMERRE)) {
                return "登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串 ";
            }
            //检查是否已存在该账户
            UserEntity user = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                    .eq("user_name", userEntity.getUserName()));

            if(user != null){
                if (userEntity.getId() == null) {
                    return "该用户名已存在";
                }
                if(null != userEntity.getId() && !userEntity.getId().equals(user.getId())){
                    if(user.getUserName().equals(userEntity.getUserName())){
                        return "该用户名已存在";
                    }
                }
            }


        }
        if (null != userEntity.getPassword()) {
            String password = userEntity.getPassword();
            if (!password.matches(Constant.PSWREX)) {
                return "密码只能为6-20个字母、数字、下划线 ";
            }
        }
        if (null != userEntity.getPhone()) {
            String phone = userEntity.getPhone();
            if (!phone.matches(Constant.MOBILEPHONEREX)) {
                return "请输入正确的手机号";
            }
            //检查手机号是否已注册
            UserEntity user = this.baseMapper.selectOne(new QueryWrapper<UserEntity>()
                    .eq("phone", userEntity.getPhone()));
            if(user != null){
                if ( userEntity.getId() == null) {
                    return "该手机号已注册";
                }
                if(null != userEntity.getId() && !userEntity.getId().equals(user.getId())){
                    if(user.getPhone().equals(userEntity.getPhone())){
                        return "该手机号已注册";
                    }
                }
            }
        }
        return null;
    }

    /**
     * @Author zehao
     * @Description //TODO 获取用户拥有的的所有权限名
     * @Date 10:23 2020/7/10
     * @Param [userId]
     * @return java.util.List<java.lang.String>
     **/
    @Override
    public List<String> getAllPermsNames(Integer userId){
        //如果用户不是超级管理员，则只查询该用户拥有的权限
        if(Constant.currentId != Constant.SUPER_ADMIN){
            List<String> menuNameList = this.baseMapper.queryAllPermsNames(userId);
            return menuNameList;
        }
        return menuDao.queryMenuNames(new QueryWrapper());
    }

    /**
     * @Author zehao
     * @Description //TODO 获取用户拥有的的所有权限Id
     * @Date 10:39 2020/7/10
     * @Param [userId]
     * @return java.util.List<java.lang.String>
     **/
    @Override
    public List<Integer> getAllPermsIds(Integer userId){
        //如果用户不是超级管理员，则只查询该用户拥有的权限
        if(Constant.currentId != Constant.SUPER_ADMIN){
            List<Integer> menuNameList = this.baseMapper.queryAllPermsIds(userId);
            return menuNameList;
        }
        return menuDao.queryMenuIds(new QueryWrapper());
    }
}
