package com.anread.user.service.impl;

import com.anread.common.dto.Result;
import com.anread.common.entity.UserAnnotation;
import com.anread.common.enums.StateEnum;
import com.anread.common.utils.CommonUtil;
import com.anread.common.vo.BookIdeaPosVO;
import com.anread.common.vo.UserAnnotationListVO;
import com.anread.common.vo.UserAnnotationVO;
import com.anread.user.mapper.UserAnnotationMapper;
import com.anread.user.service.UserAnnotationService;
import com.anread.user.utils.CfiComparators;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户标注服务实现类
 */
@Slf4j
@Service
public class UserAnnotationServiceImpl implements UserAnnotationService {

    @Autowired
    private UserAnnotationMapper userAnnotationMapper;

    @Override
    public Result upload(UserAnnotation userAnnotation) {
        if (userAnnotation == null ||
                userAnnotation.getUserId() == null ||
                userAnnotation.getBookId() == null ||
                userAnnotation.getEpubCfiRange() == null ||
                userAnnotation.getType() == null) {
            log.error("【上传标注】参数为空");
            return Result.error(StateEnum.PARAMS_ERROR);
        }

        // 同一文本可以有多个想法，但是只能有一个高亮标注
        UserAnnotation existAnnotation = userAnnotationMapper.selectOne(new LambdaQueryWrapper<UserAnnotation>()
                .eq(UserAnnotation::getEpubCfiRange, userAnnotation.getEpubCfiRange())
                .eq(UserAnnotation::getUserId, userAnnotation.getUserId())
                .eq(UserAnnotation::getBookId, userAnnotation.getBookId())
                .eq(UserAnnotation::getType, "highlight"));
        if (existAnnotation != null) {
            // 更新已存在的标注
            existAnnotation.setType(userAnnotation.getType());
            existAnnotation.setLineColor(userAnnotation.getLineColor());
            existAnnotation.setIdeaContent(userAnnotation.getIdeaContent());
            userAnnotationMapper.updateById(existAnnotation);
        } else {
            // 插入新标注
            userAnnotation.setId(CommonUtil.generateRandomID(32));
            userAnnotationMapper.insert(userAnnotation);
        }
        log.info("【上传标注】成功，数据：{}", userAnnotation);
        return Result.success();
    }

    @Override
    public Result<List<UserAnnotationListVO>> getUserAnnotationsByBookId(String userId, String bookId) {
        List<UserAnnotationListVO> userAnnotationListVOS = userAnnotationMapper.getUserAnnotationsInToc(userId, bookId);
        if (userAnnotationListVOS != null && userAnnotationListVOS.size() > 0) {
            for (UserAnnotationListVO annotationVO : userAnnotationListVOS) {
                if (annotationVO.getUserAnnotationList() != null && annotationVO.getUserAnnotationList().size() > 0) {
                    annotationVO.getUserAnnotationList().sort(CfiComparators.byCfi(UserAnnotationVO::getEpubCfiRange));
                }
            }
        }
        return Result.<List<UserAnnotationListVO>>success().data(userAnnotationListVOS);
    }

    @Override
    public Result delete(String userId, String id) {
        UserAnnotation userAnnotation = userAnnotationMapper.selectById(id);
        if (userAnnotation == null) {
            log.error("【删除标注】标注不存在，ID：{}", id);
            return Result.error("标注不存在");
        }
        if (!userAnnotation.getUserId().equals(userId)) {
            log.error("【删除标注】用户ID不匹配，标注用户ID：{}，请求用户ID：{}", userAnnotation.getUserId(), userId);
            return Result.error("用户ID不匹配");
        }
        userAnnotationMapper.deleteById(id);
        log.info("【删除标注】成功，ID：{}", id);
        return Result.success();
    }

    @Override
    public Result update(UserAnnotation userAnnotation) {
        UserAnnotation existed = userAnnotationMapper.selectById(userAnnotation.getId());
        if (existed == null) {
            log.error("【更新标注】标注不存在，ID：{}", userAnnotation.getId());
            return Result.error("标注不存在");
        }

        userAnnotationMapper.updateById(userAnnotation);
        log.info("【更新标注】成功，ID：{}", userAnnotation.getId());
        return Result.success();
    }

    @Override
    public Result<List<BookIdeaPosVO>> getPublicIdeaPos(String userId, String bookId) {
        List<BookIdeaPosVO> annotations = userAnnotationMapper.getPublicIdeaPos(userId, bookId);
        return Result.<List<BookIdeaPosVO>>success().data(annotations);
    }

    @Override
    public Result<List<UserAnnotationVO>> getPublicIdeas(String userId, String bookId, String epubCfiRange) {
        List<UserAnnotationVO> annotations = userAnnotationMapper.getPublicIdeas(bookId, epubCfiRange);
        for (UserAnnotationVO annotation : annotations) {
            annotation.setIsSelf(annotation.getUserId().equals(userId));
        }
        return Result.<List<UserAnnotationVO>>success().data(annotations);
    }
}
