package com.ityi.web.converters;

import com.ityi.common.utils.UtilFuns;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
*  自定义异常
* */
public class StringToDateConverter implements Converter<String,Date> {
    private String pattern;

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Date convert(String source) {
        try {
            //1,判断是否传入了解析规则
            if(UtilFuns.isEmpty(pattern)){
                pattern="yyyy-MM-dd";
            }
            //2.创建解析对象
            DateFormat format = new SimpleDateFormat(pattern);
            //3.解析并返回
            return format.parse(source);
        }catch (Exception e){
            throw new IllegalArgumentException("日期格式不匹配。请输入yyyy-MM-dd的格式。");
        }
    }
}
