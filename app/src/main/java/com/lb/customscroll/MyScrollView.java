package com.lb.customscroll;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.lb.customscroll.exception.ParamsErrorException;

public class MyScrollView extends ScrollView {
    private static final String TAG = "MyScrollView";
    
    private EditText search_et;
    private TextView tv_left;
    private TextView tv_right;
    private TextView center;
    private ImageView iv_left;
    private ImageView iv_right;

    private int search_et_maxHeight;
    private int search_et_maxWidth;
    private int search_et_minHeight = 150;
    private  int search_et_minWidth = 1240;
    
    private int tv_left_maxHeight;
    private int tv_left_maxWidth;
    
    private int tv_right_maxHeight;
    private int tv_right_maxWidth;

    private int center_maxHeight;
    private int center_maxWidth;
    
    private int iv_left_maxHeight;
    private int iv_left_maxWidth;
    private int iv_right_maxHeight;
    private int iv_right_maxWidth;


    //初始化数据
    private float downYPoint = 0;
    float textsize = 15;

    /**
     * 变化类型
     */
    enum ChangeType {
        /*缩小，放大*/
        Narrow, Enlarge
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderView(TextView tv_left, TextView tv_right, EditText search_et, ImageView iv_left, ImageView iv_right, TextView center) {
        //初始化
        this.search_et = search_et;
        this.tv_left = tv_left;
        this.tv_right = tv_right;
        this.iv_left = iv_left;
        this.iv_right = iv_right;
        this.center = center;
        //输入框初始高宽
        this.search_et_maxHeight = search_et.getHeight();
        this.search_et_maxWidth = search_et.getWidth();
        //左边文字框初始高宽
        this.tv_left_maxHeight = tv_left.getHeight();
        this.tv_left_maxWidth = tv_left.getWidth();
        //右边文字框初始高宽
        this.tv_right_maxHeight = tv_right.getHeight();
        this.tv_right_maxWidth = tv_right.getWidth();
        //中间隐形站位框初始高宽
        this.center_maxHeight = center.getHeight();
        this.center_maxWidth = center.getWidth();
        //左边图标初始高宽
        this.iv_left_maxHeight = iv_left.getHeight();
        this.iv_left_maxWidth = iv_left.getWidth();
        //右边图标初始高宽
        this.iv_right_maxHeight = iv_right.getHeight();
        this.iv_right_maxWidth = iv_right.getWidth();
        
        if (search_et_maxHeight <= 0 || search_et_minHeight < 0 || search_et_minHeight >= search_et_maxHeight) {
            throw new ParamsErrorException("参数错误...");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downYPoint = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE: {
                if (getScrollY() == 0) {
                    float distance = ev.getY() - downYPoint;
                    downYPoint = ev.getY();
                    if (distance < 0 && canNarrow()) {
                        measureEditView(distance, ChangeType.Narrow);
                        cancelPendingInputEvents();
                        return true;
                    } else if (distance > 0 && canEnlarge()) {
                        measureEditView(distance, ChangeType.Enlarge);
                        return true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //可以缩小
    private boolean canNarrow() {
        if (search_et == null) {
            return false;
        }
        return search_et.getHeight() > search_et_minHeight;
    }


     //可以放大
    private boolean canEnlarge() {
        if (search_et == null) {
            return false;
        }
        return search_et.getHeight() < search_et_maxHeight;
    }


     //根据移动的距离，重新计算EditView的高宽
    private void measureEditView(float distance, ChangeType changeType) {
        ViewGroup.LayoutParams params = search_et.getLayoutParams();
        //根据滑动幅度进行输入框高度的变化
        params.height = search_et.getHeight() + (int) distance;
        if (params.height < search_et_minHeight) {
            params.height = search_et_minHeight;
        } else if (params.height > search_et_maxHeight) {
            params.height = search_et_maxHeight;
        } else if (params.height < 0) {
            params.height = 0;
        }
        //根据滑动幅度进行输入框宽度的变化
        params.width = search_et.getWidth() + (int)distance;
        if(params.width < search_et_minWidth){
            params.width = search_et_minWidth;
        }else if(params.width > search_et_maxWidth){
            params.width = search_et_maxWidth;
        }else if(params.width < 0){
            params.width = 0;
        }

        measuretv_left(params.height);
        measuretv_right(params.height);
        measurecenter(params.height);
        measureiv_left(params.height);
        measureiv_right(params.height);
    }

    //重新计算中间TextView的宽和高
    private void measurecenter(int headerViewHeight) {
        if (tv_left != null) {
            int ss = headerViewHeight - search_et_minHeight;
            int zz = search_et_maxHeight - search_et_minHeight;
            double multiple = (ss * 1.00) / (zz * 1.00);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) center.getLayoutParams();
            params.height = (int) (center_maxHeight * multiple);
            params.width = (int) (center_maxWidth * multiple);
            if (params.height > center_maxHeight && params.width > center_maxWidth) {
                params.height = center_maxHeight;
                params.width = center_maxWidth;
            }
            center.setLayoutParams(params);
        }
    }

    //重新计算左面TextView的宽和高
    private void measuretv_left(int headerViewHeight) {
        if (tv_left != null) {
            int ss = headerViewHeight - search_et_minHeight;
            int zz = search_et_maxHeight - search_et_minHeight;
            double multiple = (ss * 1.00) / (zz * 1.00);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_left.getLayoutParams();
            params.height = (int) (tv_left_maxHeight * multiple);
            params.width = (int) (tv_left_maxWidth * multiple);
            float newtextsize = (float) (textsize * multiple);
            tv_left.setTextSize(newtextsize);
            if (params.height > tv_left_maxHeight && params.width > tv_left_maxWidth) {
                params.height = tv_left_maxHeight;
                params.width = tv_left_maxWidth;
            }
            tv_left.setLayoutParams(params);
        }
    }

    //重新计算右面TextView的宽和高
    private void measuretv_right(int headerViewHeight) {
        if (tv_right != null) {
            int ss = headerViewHeight - search_et_minHeight;
            int zz = search_et_maxHeight - search_et_minHeight;
            double multiple = (ss * 1.00) / (zz * 1.00);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_right.getLayoutParams();
            params.height = (int) (tv_right_maxHeight * multiple);
            params.width = (int) (tv_right_maxWidth * multiple);
            float newtextsize = (float) (textsize * multiple);
            tv_right.setTextSize(newtextsize);
            if (params.height > tv_right_maxHeight && params.width > tv_right_maxWidth) {
                params.height = tv_right_maxHeight;
                params.width = tv_right_maxWidth;
            }
            tv_right.setLayoutParams(params);
        }
    }

    //重新计算左面图标的位置
    private void measureiv_left(int headerViewHeight) {
        int rule = search_et_maxWidth / 2 - center_maxWidth / 2 - iv_left_maxWidth;
        int start = headerViewHeight * 3 - 1050;
        if (start > rule){
            start = rule;
        }else if(start < -rule){
            start = -rule;
        }
        ObjectAnimator oax = ObjectAnimator.ofFloat(iv_left, "translationX",start);
        ObjectAnimator oay = ObjectAnimator.ofFloat(iv_left, "translationY",0);
        oax.setDuration(0);
        oay.setDuration(0);
        oax.start(); //开始动画
        oay.start(); //开始动画

    }

    //重新计算右面图标的位置
    private void measureiv_right(int headerViewHeight) {
        int rule = search_et_maxWidth / 2 - center_maxWidth / 2 - iv_right_maxWidth;
        int start = - (headerViewHeight * 3 - 1050);
        if (start > rule){
            start = rule;
        }else if(start < -rule){
            start = -rule;
        }
        ObjectAnimator oax = ObjectAnimator.ofFloat(iv_right, "translationX",start);
        ObjectAnimator oay = ObjectAnimator.ofFloat(iv_right, "translationY",0);
        oax.setDuration(0);
        oay.setDuration(0);
        oax.start(); //开始动画
        oay.start(); //开始动画
    }

}
