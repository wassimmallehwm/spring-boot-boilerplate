package com.boilerplate.modules.user.service;

import java.util.*;

import com.boilerplate.configuration.exceptions.DuplicatedException;
import com.boilerplate.modules.base.BaseMapper;
import com.boilerplate.modules.base.BaseRepository;
import com.boilerplate.modules.base.BaseService;
import com.boilerplate.modules.user.domain.QUser;
import com.boilerplate.utils.datagrid.domain.GridModel;
import com.boilerplate.utils.datagrid.helpers.FilterHelper;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.dto.UserDto;
import com.boilerplate.modules.user.mapper.UserMapper;
import com.boilerplate.modules.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;


@Service
public class UserService extends BaseService<User, UserDto, Long> implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailOrUsername(username, username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username or email : " + username));
    }

    @Override
    public BaseRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public BaseMapper<User, UserDto> getMapper() {
        return userMapper;
    }

    @Override
    public String getEntityName() {
        return "User";
    }

    @Override
    public UserDto create(UserDto user) throws NotFoundException, DuplicatedException {

        Optional<User> duplicate = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        if (duplicate.isPresent()) {
            throw new DuplicatedException("User already exists");
        }
        user.setUsername(user.getEmail());
        return super.create(user);
    }

    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserDto> getPage(GridModel gridModel) {
        QUser qUser = QUser.user;
        BooleanBuilder where = new BooleanBuilder();
        where.and(qUser.id.isNotNull());
        FilterHelper.filter("user", gridModel.getFilterList(), where, User.class);

        return list(gridModel, where);
    }

    /*@Override
    public Boolean changePassword(ChangePasswordReq request) throws InvalidPassword, NotFoundException {
        Admin admin = utilsService.getCurrentAdmin();
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new InvalidPassword("Invalid password!");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPassword("New password and confirmation password must match!");
        }
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminRepository.save(admin);
        return true;
    }*/

    /*@Override
    public byte[] export(Long projectId){
        List<UserExport> exports = new ArrayList<>();
        QUser qUser = QUser.user;
        BooleanBuilder where = new BooleanBuilder();
        where.and(qUser.project.id.eq(projectId));
        Iterable<User> list = userRepository.findAll(where.getValue());
        for (User user : list) {
            exports.add(new UserExport(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getGroups().stream().map(Group::getName).collect(Collectors.joining(", ")),
                    Objects.nonNull(user.getOffended()) && user.getOffended() ? "Yes" : "No",
                    Objects.nonNull(user.getSimulated()) && user.getSimulated() ? "Yes" : "No",
                    Objects.nonNull(user.getCompromised()) && user.getCompromised() ? "Yes" : "No"
            ));
        }
        return WriteExcelFile.writeToExcel(exports);
    }*/
}
