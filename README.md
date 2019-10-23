# XfermodeBar
贝塞尔波纹+蒙版和螺旋线进度条

最近有小伙伴在问我画笔`Paint`蒙版的叠加模式。刚好之前搞过，所以就整理了下代码，把控件提取了出来。晚些时候提交上来。

## 1.效果预览

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/all.gif?raw=true)

## 2.使用

### WaveShapeBar 浪

```java
  wpv.setText(Color.parseColor("#FFFFFF"), 120)
            .setWaveColor(Color.parseColor("#FF0000"))
            .setSpeed(10)//8
            .setMaxProgress(100)
            .build();
```

```xml
<cos.mos.xfermodebar.widget.WaveShapeBar
        android:id="@+id/st3_wpv"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:background="@drawable/ic_heart"/>
```

### LineBar 螺旋线 蒙版心跳

```xml
<cos.mos.xfermodebar.widget.LineBar
        android:id="@+id/st3_line"
        android:layout_width="160dp"
        android:layout_height="160dp"
        android:layout_gravity="center"
        android:background="@drawable/ic_heart"/>
```

## 3.说说咋搞的

##### 3.1.先说这个线

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/lineshow.png?raw=true)

> 计算出坐标系中，以矩形为例。四条的坐标分布如图。
>
> 边长为为a的这货，在distance递增的时候，对应直线的坐标则是(0,distance)-->(distance,a)
>
> 以此类推：

```java
switch (pointor) {
    case 0:
        x += distance;
        path.lineTo(x, 0);
        break;
    case 1:
        y += distance;
        path.lineTo(0, y);
        break;
    case 2:path.lineTo(0, height - y);break;
    case 3:path.lineTo(x, height);break;
    case 4:path.lineTo(width - x, height);break;
    .....
}
```

> 将这个路径运动起来就是这个样子

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/line.gif?raw=true)

##### 3.2.画笔`Paint`的叠加模式`Xfermode`

> 两张图上下叠加，我下面直接叫【上层图】和【下层图】（实在觉得抽象的话，可以打开ps多戳戳试试

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/ps.png?raw=true)

##### 叠加方式：`Paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP))`

- 1.`ADD`:饱和相加,对图像饱和度进行相加
- 2.`CLEAR`:清除图像
- 3.`DARKEN`:变暗,较深的颜色覆盖较浅的颜色，若两者深浅程度相同则混合
- 4.`LIGHTEN`:变亮，与DARKEN相反，DARKEN和LIGHTEN生成的图像结果与Android对颜色值深浅的定义有关
- 5.`MULTIPLY`:正片叠底像素颜色值=【上层图】素颜色值 x 【下层图】素颜色值 ÷ 255
- 6.`OVERLAY`:叠加
- 7.`SCREEN`:滤色，色调均和,保留两图中较白的部分，遮盖较暗的部分
- 8.`XOR`:两图相交处之外绘制它们，相交处受对应alpha和色值影响，如果:不透明=>相交处不绘制

##### 混合（反向）

-  1.`SRC`:只显示【上层图】
-  2.`SRC_ATOP`:两图相交处绘制【上层图】,不相交处绘制【下层图】,效果受两图alpha影响
-  3.`SRC_IN`:只在两图相交处绘制【上层图】
-  4.`SRC_OUT`:只在两图不相交处绘制【上层图】，相交处根据【下层图】的对应地方的alpha进行过滤，【下层图】:不透明=>完全过滤,全透明=>不过滤
-  5.`SRC_OVER`:将【上层图】放在【下层图】上方

##### 混合

- 1.`DST`:只显示【下层图】
- 2.`DST_OVER`:将【下层图】 放在【上层图】上方
- 3.`DST_ATOP`:两图相交处绘制【下层图】，不相交处绘制【上层图】，效果受两图alpha影响
- 4.`DST_IN`:只在两图相交处绘制【下层图】，效果受【上层图】对应地方alpha影响
- 5.`DST_OUT`:只在两图不相交处绘制【下层图】,相交处根据【上层图】alpha进行过滤,【上层图】:不透明=>完全过滤,全透明=>不过滤

##### 3.3.关于硬件加速的3种模式

 - 1.`LAYER_TYPE_SOFTWARE`
 - - 1.开了一个buffer，把View画到这个buffer上面去
 - - 2.渲染到Bitmap（无论硬件加速是否打开，都会有一张Bitmap（software layer）
 - - 3.优点：在进行动画，使用software可以只画一次View树，很省。
 - - 4.缺点：硬件加速打开时，更耗时（因为bmp渲染完后，还要渲染到hardware layer上
 - 2.`LAYER_TYPE_HARDWARE`
 - - 1.开了一个buffer，把View画到这个buffer上面去
 - - 2.硬件加速关闭时，作用同software。
 - - 3.硬件加速打开时，会在FBO（`Framebuffer Object`）上面做渲染，在进行动画时，View树也只需要画一次。
 - 3.`LAYER_TYPE_NONE`
 - - 不为这个View树建立单独的layer

##### 3.4.蒙版

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/heart.png?raw=true)

> - 1.先给控件设置一个背景`background`，
> - 2.然后在步骤`3.1`计算好的线的基础上
> - 3.给画笔设置混合模式进行绘制
> - 4.这里就举3个栗子

- 栗子1：`PorterDuff.Mode.DST_ATOP`--根据alpha绘制`相交`的`下图`，和`不相交`的`上图`

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/DST_ATOP.gif?raw=true)

- 栗子2：`PorterDuff.Mode.SCREEN`--滤色，色调均和,两图中颜色`明暗混合`
- ![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/SCREEN.gif?raw=true)
- 栗子3：`PorterDuff.Mode.SRC_ATOP`--根据alpha绘制`相交`的`上图`，和`不相交`的`下图`(和第一个栗子相反)

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/SRC_ATOP.gif?raw=true)

#### 4.那个浪

##### 4.1.关于`浪`的绘制

> 这里是一个二阶贝塞尔曲线方程（懒得画图了，就干说）

- 1.先根据矩形的宽确定波的半径：`waveRadius`

- 2.根据矩形的高确定波的运动帧数：`waveNum=（width / ((int) waveRadius * 2) + 1）`

- 3.二阶贝塞尔计算波的轮廓:

- - 这里有2段二阶贝塞尔拼接的（还是整一个图嘛，不过qq截图不能画曲线，手画有点歪）
  - 正弦sina与X轴交点为`3个起始点`（2段，每段各2个）
  - 正弦90度处`2个控制点`（每段各一个）
- 4.看码（关键变量见注释）
  

  ![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/sina.png?raw=true)

```java
path.reset();
path.moveTo(0, CurY);//动画原点
int waveNum = width / ((int) waveRadius * 2) + 1;//运动总帧数
int multiplier = 0;//倍率
for (int i = 0; i < waveNum; i++) {
    //二阶贝塞尔曲线
    path.quadTo(waveRadius * (multiplier + 1) - distance, CurY - waveRadius / 2,
    waveRadius * (multiplier + 2) - distance, CurY);
    path.quadTo(waveRadius * (multiplier + 3) - distance, CurY + waveRadius / 2,
    waveRadius * (multiplier + 4) - distance, CurY);
    multiplier += 4;
}
distance += waveRadius / speed;
distance %= waveRadius * 4;
path.lineTo(width, height);
path.lineTo(0, height);
path.close();//轨迹闭合
```

- 5.先不加蒙版让它动起来

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/ren_no.gif?raw=true)

- 6.加上上面那第三个栗子的蒙版：`PorterDuff.Mode.SRC_ATOP`

![demo1](https://github.com/KosmoSakura/XfermodeBar/blob/master/img/red.gif?raw=true)

### 好了，就这样