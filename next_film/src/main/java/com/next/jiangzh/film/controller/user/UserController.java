package com.next.jiangzh.film.controller.user;

import com.next.jiangzh.film.common.utils.ToolUtils;
import com.next.jiangzh.film.controller.common.BaseResponseVO;
import com.next.jiangzh.film.controller.common.TraceUtil;
import com.next.jiangzh.film.controller.exception.NextFilmException;
import com.next.jiangzh.film.controller.exception.ParamErrorException;
import com.next.jiangzh.film.controller.user.vo.EnrollUserVO;
import com.next.jiangzh.film.controller.user.vo.UserInfoVO;
import com.next.jiangzh.film.service.common.exception.CommonServiceExcetion;
import com.next.jiangzh.film.service.user.UserServiceAPI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user/")
@Api("用户模块相关的 API")
public class UserController {

    @Autowired
    private UserServiceAPI userServiceAPI;

    @ApiOperation(value = "用户名重复性验证",notes = "用户名重复性验证")
    @ApiImplicitParam(name = "username",
            value = "待验证的用户名称",
            paramType = "query", required = true, dataType = "string")
    @RequestMapping(value = "check",method = RequestMethod.POST)
    public BaseResponseVO checkUser(String username) throws CommonServiceExcetion, NextFilmException {
        if(ToolUtils.isEmpty(username)){
            throw new NextFilmException(1,"username不能为空");
        }
        boolean hasUserName = userServiceAPI.checkUserName(username);
        if(hasUserName){
            return BaseResponseVO.serviceFailed("用户名已存在");
        }else{
            return BaseResponseVO.success();
        }
    }

    @ApiOperation(value = "用户信息注册",notes = "用户名、密码和手机号必须填写")
    @ApiImplicitParam(name = "enrollUserVO",
            value = "用户登记注册信息实体", required = true, dataType = "EnrollUserVO")
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public BaseResponseVO register(@RequestBody EnrollUserVO enrollUserVO) throws CommonServiceExcetion, ParamErrorException {
        // 领域模型 贫血模型 - 充血模型
        enrollUserVO.checkParam();

        userServiceAPI.userEnroll(enrollUserVO);

        return BaseResponseVO.success();
    }

    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    public BaseResponseVO describeUserInfo() throws CommonServiceExcetion, ParamErrorException {

        String userId = TraceUtil.getUserId();

        UserInfoVO userInfoVO = userServiceAPI.describeUserInfo(userId);

        userInfoVO.checkParam();

        return BaseResponseVO.success(userInfoVO);
    }


    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    public BaseResponseVO updateUserInfo(@RequestBody UserInfoVO userInfoVO) throws CommonServiceExcetion, ParamErrorException {

        userInfoVO.checkParam();

        UserInfoVO result = userServiceAPI.updateUserInfo(userInfoVO);

        userInfoVO.checkParam();

        return BaseResponseVO.success(result);
    }

    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public BaseResponseVO logout() throws CommonServiceExcetion, ParamErrorException {

        String userId = TraceUtil.getUserId();

        /*
            1、用户信息放入Redis缓存
            2、去掉用户缓存
         */

        return BaseResponseVO.success();
    }

}
