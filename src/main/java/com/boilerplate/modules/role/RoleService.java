package com.boilerplate.modules.role;

import com.boilerplate.modules.base.BaseMapper;
import com.boilerplate.modules.base.BaseRepository;
import com.boilerplate.modules.base.BaseService;
import com.boilerplate.modules.base.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService extends BaseService<Role, RoleDto, Long> implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public RoleDto findByName(String name) {
        return roleMapper.toDto(roleRepository.findOneByName(name));
    }
    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    @Override
    public BaseMapper<Role, RoleDto> getMapper() {
        return roleMapper;
    }

    @Override
    public String getEntityName() {
        return "Role";
    }
}
