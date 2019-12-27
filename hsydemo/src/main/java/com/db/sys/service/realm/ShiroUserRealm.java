package com.db.sys.service.realm;

import com.db.common.utils.ShiroUtils;
import com.db.sys.dao.SysMenuDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.dao.SysUserDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


//本模块的service可以借助realm实现，
//我们编写realm时可以继承AuthorizingRealm并重写相关方法完成相关业务的实现。
@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	
	//密码设置类型
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//设置凭证匹配器
		HashedCredentialsMatcher cMatcher =
				new HashedCredentialsMatcher();
		cMatcher.setHashAlgorithmName("MD5");
		super.setCredentialsMatcher(cMatcher);
	}
	
	
	
	
	
	//认证信息的获取和封装
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
	
			
			//1.获取客户端提交的用户信息
				UsernamePasswordToken uToken=
				(UsernamePasswordToken)token;
				String username=uToken.getUsername();
				//2.基于用户名从数据库查询用户信息
				SysUser user=sysUserDao.findUserByUserName(username);
				//3.校验用户信息(用户存在吗)
				if(user==null)
					//
				throw new UnknownAccountException();
				if(user.getValid()==0)
				throw new LockedAccountException();
				//4.对用户信息进行封装
				ByteSource credentialsSalt=
				ByteSource.Util.bytes(user.getSalt());
				SimpleAuthenticationInfo info=
				new SimpleAuthenticationInfo(
				user, //principal(用户身份)
				user.getPassword(),//hashedCredentials(已经加密的密码)
				credentialsSalt, //credentialsSalt(盐)
				this.getName());//realm name
				return info;//此对象返回给谁了?认证管理器
	
	}
		//授权信息的获取和封装
	
	private Map<String,AuthorizationInfo> authorizationCache=
			new ConcurrentHashMap<String, AuthorizationInfo>();


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
			//1.获取登录用户信息 把用户名放入key
			SysUser user = (SysUser) principals.getPrimaryPrincipal();
			//先获数据库取缓存,如果数据库没有就执行下一步
			//把用户名放入key
			AuthorizationInfo aInfo=
					authorizationCache.get(user.getUsername());
					if(aInfo!=null)return aInfo;
					System.out.println("===doGetAuthorizationInfo==");

			//2.基于用户信息获取对应的角色信息ID
			//List<Integer> roleIds=
			List<Integer> roleIds 
			= sysUserRoleDao.findRoleIdsByUserId(user.getId());
			if(roleIds==null||roleIds.size()==0)
				throw new AuthorizationException();
			//3.基于用户和角色信息获得对应的菜单ID
			//List<Integer> menuIds=
			Integer[] array={};
			List<Integer> menuIds=
			sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
			if(menuIds==null||menuIds.size()==0)
				throw new AuthorizationException();
			//4.基于菜单id获取对应得权限标示
			List<String> permisssions=
					sysMenuDao.findPermissions(
							menuIds.toArray(array));
			if(permisssions==null||permisssions.size()==0)
				throw new AuthorizationException();
			//5.对用户权限进行封装并返回(授权管理器)
			Set<String> stringPermissions=
					new HashSet<String>();
					for(String p:permisssions){
						if(!StringUtils.isEmpty(p)){
							stringPermissions.add(p);
						}
					}//去重和空(null),空串
					
					
				SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
				info.setStringPermissions(stringPermissions);
				//把缓存添加到数据库
			authorizationCache.put(user.getUsername(),info);

					
		return info;
	}
	/***
	 * 执行退出操作
	 */
	public void logout(){
		SysUser user=ShiroUtils.getUser();
		authorizationCache.remove(user.getUsername());
		//系统底层会将用户从shiro中的session对象移除
   	    SecurityUtils.getSubject().logout();
	}

}
