package com.seasonfif.arecyclerview;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.seasonfif.arecyclerview.Empty.FOOTER_COVER;
import static com.seasonfif.arecyclerview.Empty.FULL_COVER;
import static com.seasonfif.arecyclerview.Empty.HEADER_COVER;

/**
 * 创建时间：2017年07月06日17:08 <br>
 * 作者：zhangqiang <br>
 * 描述：空白页位置
 */

@IntDef({HEADER_COVER,
        FOOTER_COVER,
        FULL_COVER})

@Retention(RetentionPolicy.SOURCE)
public @interface Empty {

    /**
     * 覆盖header
     */
    int HEADER_COVER = 0x000f;

    /**
     * 覆盖footer
     */
    int FOOTER_COVER = 0x00f0;

    /**
     * 全部覆盖
     */
    int FULL_COVER = HEADER_COVER | FOOTER_COVER;

}
