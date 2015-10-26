package com.cmu.services;

import com.cmu.interfaces.RoleDao;
import com.cmu.interfaces.RoleService;
import com.cmu.model.Role;

public class RoleServiceImpl implements RoleService {
    
    private RoleDao roleDao;
 
    public Role getRole(int id) {
        return roleDao.getRole(id);
    }
 
}