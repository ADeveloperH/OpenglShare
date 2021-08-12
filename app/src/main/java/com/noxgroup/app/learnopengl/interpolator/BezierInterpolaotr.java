package com.noxgroup.app.learnopengl.interpolator;

/**
 * @author huangjian
 * @create 2021/8/12
 * @Description
 */
public class BezierInterpolaotr implements Interpolator {

    private final double ZERO_LIMIT = 1e-6;//精度
    private float ax;
    private float bx;
    private float cx;
    private float ay;
    private float by;
    private float cy;

    public BezierInterpolaotr(float p1x, float p1y, float p2x, float p2y) {
        // Calculate the polynomial coefficients,
        // implicit first and last control points are (0,0) and (1,1).
        ax = 3 * p1x - 3 * p2x + 1;//一元三次方程中的 a
        bx = 3 * p2x - 6 * p1x;//一元三次方程中的 b
        cx = 3 * p1x;//一元三次方程中的 c

        ay = 3 * p1y - 3 * p2y + 1;//一元三次方程中的 a
        by = 3 * p2y - 6 * p1y;//一元三次方程中的 b
        cy = 3 * p1y;//一元三次方程中的 c
    }

    /**
     * 根据 t
     * 求一元三次方程的求导后的函数 的值
     *
     * @param t
     * @return
     */
    private float sampleCurveDerivativeX(float t) {
        // `ax t^3 + bx t^2 + cx t' expanded using Horner 's rule.
        return (3 * ax * t + 2 * bx) * t + cx;
    }

    /**
     * 一元三次方程根据 t 求 x
     *
     * @param t
     * @return
     */
    private float sampleCurveX(float t) {
        return ((ax * t + bx) * t + cx) * t;
    }

    /**
     * 一元三次方程根据 t 求 y
     *
     * @param t
     * @return
     */
    private float sampleCurveY(float t) {
        return ((ay * t + by) * t + cy) * t;
    }

    /**
     * Given an x value, find a parametric value it came from.
     * 给定一个 x 值，求解 t
     *
     * @param x
     * @return
     */
    private float solveCurveX(float x) {
        float t2 = x;//初始值为输入的 x
        float derivative;
        float x2;

        //求解 x 时刻 t 的解，当前的 x 值可看作已知值
        // https://trac.webkit.org/browser/trunk/Source/WebCore/platform/animation
        // First try a few iterations of Newton's method -- normally very fast.
        // 首先尝试几次牛顿方法的迭代——通常非常快
        // http://en.wikipedia.org/wiki/Newton's_method
        //牛顿法求解 f(x) = 0 的解。当前输入的值是 x 为已知值可看作常数
        //例如 ：y= x^2 当 y=4 时求解 x ，也就是求解 x^2 - 4 = 0 的解
        for (int i = 0; i < 8; i++) {
            // f(t)-x=0
            x2 = sampleCurveX(t2) - x;
            if (Math.abs(x2) < ZERO_LIMIT) {
                return t2;
            }
            derivative = sampleCurveDerivativeX(t2);
            // == 0, failure
            /* istanbul ignore if */
            if (Math.abs(derivative) < ZERO_LIMIT) {
                break;
            }
            t2 -= x2 / derivative;
        }

        // Fall back to the bisection method for reliability.
        // 回到二分法以获得可靠性
        // bisection
        // http://en.wikipedia.org/wiki/Bisection_method
        float t1 = 1;
        /* istanbul ignore next */
        float t0 = 0;

        /* istanbul ignore next */
        t2 = x;
        /* istanbul ignore next */
        while (t1 > t0) {
            x2 = sampleCurveX(t2) - x;
            if (Math.abs(x2) < ZERO_LIMIT) {
                return t2;
            }
            if (x2 > 0) {
                t1 = t2;
            } else {
                t0 = t2;
            }
            t2 = (t1 + t0) / 2;
        }
        // Failure
        return t2;
    }

    //输入当前进度 x,求出插值后的动画真实进度
    public float solve(float x) {
        if (x == 0 || x == 1){
            return sampleCurveY(x);
        }
        return sampleCurveY(solveCurveX(x));
    }

    @Override
    public float getInterpolation(float input) {
        return solve(input);
    }
}
