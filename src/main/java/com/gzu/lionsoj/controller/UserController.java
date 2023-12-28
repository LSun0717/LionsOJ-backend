package com.gzu.lionsoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzu.lionsoj.annotation.AuthCheck;
import com.gzu.lionsoj.constant.UserConstant;
import com.gzu.lionsoj.exception.BusinessException;
import com.gzu.lionsoj.exception.ThrowUtils;
import com.gzu.lionsoj.model.vo.LoginUserVO;
import com.gzu.lionsoj.model.vo.UserVO;
import com.gzu.lionsoj.common.BaseResponse;
import com.gzu.lionsoj.common.DeleteRequest;
import com.gzu.lionsoj.common.ErrorCode;
import com.gzu.lionsoj.common.ResultUtils;
import com.gzu.lionsoj.config.WxOpenConfig;
import com.gzu.lionsoj.model.dto.user.UserAddRequest;
import com.gzu.lionsoj.model.dto.user.UserLoginRequest;
import com.gzu.lionsoj.model.dto.user.UserQueryRequest;
import com.gzu.lionsoj.model.dto.user.UserRegisterRequest;
import com.gzu.lionsoj.model.dto.user.UserUpdateMyRequest;
import com.gzu.lionsoj.model.dto.user.UserUpdateRequest;
import com.gzu.lionsoj.model.entity.User;
import com.gzu.lionsoj.service.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname: UserController
 * @Description: 用户api
 * @Author: lions
 * @Datetime: 12/28/2023 11:45 PM
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private WxOpenConfig wxOpenConfig;

    /**
     * @Description: 用户注册api
     * @param userRegisterRequest 用户注册请求封装
     * @Return: 新注册用户id
     * @Author: lions
     * @Datetime: 12/28/2023 11:46 PM
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * @Description: 用户登录api
     * @param userLoginRequest 用户登录请求封装
     * @param request http请求
     * @Return: 登录用户视图对象
     * @Author: lions
     * @Datetime: 12/28/2023 11:47 PM
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * @Description: 用户登录api（微信开放平台）
     * @param request http请求
     * @param response http响应
     * @param code 验证码
     * @Return: 登录用户视图对象
     * @Author: lions
     * @Datetime: 12/28/2023 11:48 PM
     */
    @GetMapping("/login/wx_open")
    public BaseResponse<LoginUserVO> userLoginByWxOpen(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       @RequestParam("code") String code) {
        WxOAuth2AccessToken accessToken;
        try {
            WxMpService wxService = wxOpenConfig.getWxMpService();
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, code);
            String unionId = userInfo.getUnionId();
            String mpOpenId = userInfo.getOpenid();
            if (StringUtils.isAnyBlank(unionId, mpOpenId)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
            }
            return ResultUtils.success(userService.userLoginByMpOpen(userInfo, request));
        } catch (Exception e) {
            log.error("userLoginByWxOpen error", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
        }
    }

    /**
     * @Description: 用户注销api
     * @param request http请求
     * @Return: 是否注销成功
     * @Author: lions
     * @Datetime: 12/28/2023 11:49 PM
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * @Description: 获取当前登录用户api
     * @param request http请求
     * @Return: 当前登录用户视图对象
     * @Author: lions
     * @Datetime: 12/28/2023 11:50 PM
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * @Description: 创建用户api
     * @param userAddRequest 用户新增请求封装
     * @param request http请求
     * @Return: 新增用户id
     * @Author: lions
     * @Datetime: 12/28/2023 11:51 PM
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * @Description: 删除用户api
     * @param deleteRequest 删除用户请求封装
     * @param request http请求
     * @Return: 是否删除成功
     * @Author: lions
     * @Datetime: 12/28/2023 11:52 PM
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * @Description: 更新用户信息api
     * @param userUpdateRequest 更新用户信息请求封装
     * @param request http请求
     * @Return: 是否更新成功
     * @Author: lions
     * @Datetime: 12/28/2023 11:53 PM
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * @Description: 根据 id 获取用户api（仅管理员）
     * @param id 用户id
     * @param request http请求
     * @Return: 用户全量信息
     * @Author: lions
     * @Datetime: 12/28/2023 11:54 PM
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * @Description: 根据 id 获取用户视图对象
     * @param id 用户id
     * @param request http请求
     * @Return: 用户视图对象
     * @Author: lions
     * @Datetime: 12/28/2023 11:55 PM
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * @Description: 分页获取用户全量信息列表（仅管理员）
     * @param userQueryRequest 用户查询请求封装
     * @param request http请求封装
     * @Return: 用户全量信息分页
     * @Author: lions
     * @Datetime: 12/28/2023 11:55 PM
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * @Description: 分页获取用户视图对象
     * @param userQueryRequest 用户查询请求封装
     * @param request http请求
     * @Return: 用户视图对象分页
     * @Author: lions
     * @Datetime: 12/28/2023 11:56 PM
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * @Description: 更新用户信息
     * @param userUpdateMyRequest 用户更新请求封装
     * @param request http请求封装
     * @Return: 是否更新成功
     * @Author: lions
     * @Datetime: 12/28/2023 11:57 PM
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
