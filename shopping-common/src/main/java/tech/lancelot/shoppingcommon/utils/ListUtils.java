package tech.lancelot.shoppingcommon.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    /**
     * 集合对象属性拷贝
     *
     * @param sourceList
     * @param clazz
     * @param <S>
     * @param <D>
     * @return
     * @throws Exception
     */
    public static <S, D> List<D> copyProperties(List<S> sourceList, Class<D> clazz) throws Exception {
        List<D> destinationList = new ArrayList<>();
        for (S productInfo : sourceList) {
            D destination = clazz.newInstance();
            BeanUtils.copyProperties(productInfo, destination);
            destinationList.add(destination);
        }
        return destinationList;
    }
}
