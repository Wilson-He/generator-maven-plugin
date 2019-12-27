package io.github.generator;

import com.baomidou.mybatisplus.generator.config.po.TableFieldComment;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Wilson
 * @date 2019/10/15
 **/
@Data
public class ConstantCommentConfig {
    private String keyStartSign;
    private String keyEndSign;
    private String valStartSign;
    private String valEndSign;
    private String commentStartSign;
    private String commentEndSign;
    private String regexp;
    private Integer valIndex;
    private Integer keyOrder;
    private Integer valOrder;
    private Integer commentOrder;

    private static final int MAX_PATTERN_LENGTH = 5;
    private static final String COMMENT_FORMAT_PATTERN = ",%s,";
    public static final String SEPARATOR = ",";
    private String[] separators;
    public static List<String> EXCLUDE_CONSTANT_FIELDS;

    private static ConstantCommentConfig config;

    public static void initInstance(String pattern, List<String> excludeConstantFields) {
        if (pattern == null || config != null) {
            return;
        }
        ConstantCommentConfig.EXCLUDE_CONSTANT_FIELDS = excludeConstantFields;
        config = new ConstantCommentConfig();
        int length = pattern.length();
        if (length < MAX_PATTERN_LENGTH) {
            throw new InvalidParameterException("数据库字段常量pattern错误");
        }
        int keyIndex = pattern.indexOf("key");
        int keyLength = "key".length();
        config.keyStartSign = keyIndex > 0 ? pattern.substring(keyIndex - 1, keyIndex) : SEPARATOR;
        if (keyIndex == 0) {
            config.keyOrder = 1;
            config.keyStartSign = SEPARATOR;
        } else {
            config.keyOrder = keyIndex + keyLength == length ? 3 : 2;
            config.keyStartSign = pattern.substring(keyIndex - 1, keyIndex);
        }
        config.keyEndSign = keyIndex + keyLength != length ? pattern.substring(keyIndex + keyLength, keyIndex + keyLength + 1) : SEPARATOR;
        config.valIndex = pattern.indexOf("value");
        int valueLength = "value".length();
        if (config.valIndex == 0) {
            config.valOrder = 1;
            config.valStartSign = SEPARATOR;
        } else {
            config.valOrder = config.valIndex + valueLength == length ? 3 : 2;
            config.valStartSign = pattern.substring(config.valIndex - 1, config.valIndex);
        }
        config.valEndSign = config.valIndex + valueLength != length ? pattern.substring(config.valIndex + valueLength, config.valIndex + valueLength + 1) : SEPARATOR;
        int commentIndex = pattern.indexOf("comment");
        int commentLength = "comment".length();
        if (commentIndex == 0) {
            config.commentOrder = 1;
            config.commentStartSign = SEPARATOR;
        } else {
            config.commentOrder = commentIndex + commentLength == length ? 3 : 2;
            config.commentStartSign = pattern.substring(commentIndex - 1, commentIndex);
        }
        config.commentEndSign = commentIndex + commentLength != length ? pattern.substring(commentIndex + commentLength, commentIndex + commentLength) : SEPARATOR;
        String separatorRegexp = Stream.of(config.keyStartSign, config.keyEndSign,
                config.valStartSign, config.valEndSign, config.commentStartSign, config.commentEndSign)
                .filter(sign -> !SEPARATOR.equals(sign) && !"".equals(sign))
                .collect(Collectors.toSet())
                .stream()
                .reduce((a, b) -> a + "|" + b)
                .orElse("fetch separator error");
        config.regexp = String.format(".+(%s).+", separatorRegexp);
        config.separators = separatorRegexp.split("\\|");
    }

    public static ConstantCommentConfig getInstance() {
        return config;
    }

    public List<TableFieldComment> fetchCommentList(String rawString, String clazz) {
        String[] comments = rawString.split(",");
        if (comments.length == 1) {
            return Collections.emptyList();
        }
        List<TableFieldComment> list = new ArrayList<>(comments.length);
        String keyCommentSeparator = null;
        boolean justKeyComment = false;
        for (String separator : separators) {
            if (!comments[0].contains(separator)) {
                justKeyComment = true;
            } else {
                keyCommentSeparator = separator;
            }
        }
        for (String comment : comments) {
            // key:comment  key:value:comment
            comment = String.format(COMMENT_FORMAT_PATTERN, comment);
            String value;
            if (justKeyComment) {
                assert keyCommentSeparator != null;
                int separatorIndex = comment.indexOf(keyCommentSeparator);
                value = StringUtils.substring(comment, 1, separatorIndex);
                String valComment = StringUtils.substring(comment, separatorIndex + 1, comment.length() - 1);
                list.add(new TableFieldComment(value, value, valComment, clazz));
                continue;
            } else {
                value = fetch(valOrder, comment, valStartSign, valEndSign);
            }
            String key = StringUtils.substringBetween(comment, keyStartSign, keyEndSign);
            String valComment = fetch(commentOrder, comment, commentStartSign, commentEndSign);
            list.add(new TableFieldComment(key, value, valComment, clazz));
        }
        return list;
    }

    private String fetch(int order, String comment, String startSign, String endSign) {
        // ,comment:key:value:,  ,value:comment:key, ,key:value:comment,
        switch (order) {
            case 1:
                return StringUtils.substringBetween(comment, startSign, endSign).substring(1);
            case 2:
                return comment.substring(comment.indexOf(startSign) + 1, comment.lastIndexOf(endSign));
            case 3:
                return comment.substring(comment.lastIndexOf(startSign) + 1, comment.length() - 1);
            default:
                return "";
        }
    }
}
