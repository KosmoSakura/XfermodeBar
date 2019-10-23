# XfermodeBar
贝塞尔+蒙版的波纹和螺旋线进度条

最近有小伙伴在问我画笔`Paint`蒙版的叠加模式。刚好之前搞过，所以就整理了下代码，把控件提取了出来。晚些时候提交上来。



## 1.效果预览

晚点

## 2.WaveShapeBar使用

```java
 wpv.setText(ContextCompat.getColor(this, R.color.white), UScreen.dp2px(40))
            .setWaveColor(ContextCompat.getColor(this, R.color.fun_txt_pink))
            .setSpeed(10)//8
            .setMaxProgress(100)
            .build();
```

```xml
<cos.mos.utils.widget.progress.WaveShapeBar
        android:id="@+id/st3_wpv"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:background="@drawable/ic_heart"/>
```

## 3.LineBar 螺旋线 蒙版心跳

```xml
<com.duang.love_match.widget.LineBar
            android:id="@+id/st3_line"
            android:layout_width="160dp"
            android:layout_height="160dp"
            android:layout_gravity="center"
            android:background="@drawable/ic_ht_3"/>
```

## 4.CircleBar呼吸灯

```xml
<cos.mos.utils.widget.progress.CircleBar
        android:layout_width="110dp"
        android:layout_height="110dp"
        kosmos:cb_max_progress="100"
        kosmos:cb_progress="10"
        kosmos:inside_color="#ffffff"
        kosmos:outside_color="#ea5413"
        kosmos:outside_radius="40dp"
        kosmos:progress_text_size="30dp"
        kosmos:progress_width="4dp"/>
```

## 5.说说原理

晚点和代码一起吧。