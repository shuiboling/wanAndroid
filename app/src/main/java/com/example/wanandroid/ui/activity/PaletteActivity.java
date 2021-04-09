package com.example.wanandroid.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.palette.graphics.Target;

import com.example.wanandroid.R;
import com.example.wanandroid.framework.mvp.BaseActivity;
import com.example.wanandroid.framework.mvp.RxPresenter;

import butterknife.BindView;

public class PaletteActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.vibrant)
    TextView tvVibrant;
    @BindView(R.id.vibrantDark)
    TextView tvVibrantDark;
    @BindView(R.id.vibrantLight)
    TextView tvVibrantLight;
    @BindView(R.id.muted)
    TextView tvMuted;
    @BindView(R.id.mutedDark)
    TextView tvMutedDark;
    @BindView(R.id.mutedLight)
    TextView tvMutedLight;
    @BindView(R.id.bitmap)
    ImageView imageView;
    @BindView(R.id.text)
    TextView text;

    @Override
    public int getLayout() {
        return R.layout.palette_activity;
    }

    @Override
    public void initEventAndView() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pic_3);
        Bitmap bitmap = ((BitmapDrawable)(imageView.getDrawable())).getBitmap();
        // Synchronous
        Palette p = Palette.from(bitmap).generate();

        // Asynchronous
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int vibrantColor = palette.getVibrantColor(Color.RED);//如果分析不出来，则则返回默认颜色Color.BLUE
                int vibrantDarkColor = palette.getDarkVibrantColor(Color.RED);
                int vibrantLightColor = palette.getLightVibrantColor(Color.RED);
                int mutedColor = palette.getMutedColor(Color.RED);
                int mutedDarkColor = palette.getDarkMutedColor(Color.RED);
                int mutedLightColor = palette.getLightMutedColor(Color.RED);

                tvVibrant.setBackgroundColor(vibrantColor);
                tvVibrantDark.setBackgroundColor(vibrantDarkColor);
                tvVibrantLight.setBackgroundColor(vibrantLightColor);
                tvMuted.setBackgroundColor(mutedColor);
                tvMutedDark.setBackgroundColor(mutedDarkColor);
                tvMutedLight.setBackgroundColor(mutedLightColor);

                Palette.Swatch swatch = palette.getMutedSwatch();
                int rgb = swatch.getRgb();
                int textColor = swatch.getBodyTextColor();
                int titleTextColor = swatch.getTitleTextColor();
                int color = getTranslucentColor(0.8f,rgb);
                text.setBackgroundColor(color);
                text.setTextColor(textColor);
                tvTitle.setBackgroundColor(titleTextColor);
            }

        });

        Target.Builder builder = new Target.Builder();
        builder.setExclusive(true);
        builder.setTargetLightness(0.5f);

        Palette.from(bitmap).addTarget(builder.build()).resizeBitmapArea(2).maximumColorCount(8).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(Color.RED);//如果分析不出来，则则返回默认颜色Color.BLUE
                int vibrantDarkColor = palette.getDarkVibrantColor(Color.RED);
                int vibrantLightColor = palette.getLightVibrantColor(Color.RED);
                int mutedColor = palette.getMutedColor(Color.RED);
                int mutedDarkColor = palette.getDarkMutedColor(Color.RED);
                int mutedLightColor = palette.getLightMutedColor(Color.RED);

                tvVibrant.setBackgroundColor(vibrantColor);
                tvVibrantDark.setBackgroundColor(vibrantDarkColor);
                tvVibrantLight.setBackgroundColor(vibrantLightColor);
                tvMuted.setBackgroundColor(mutedColor);
                tvMutedDark.setBackgroundColor(mutedDarkColor);
                tvMutedLight.setBackgroundColor(mutedLightColor);

                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null){
                    int bodyTextColor = swatch.getBodyTextColor();
                    int titleTextColor = swatch.getTitleTextColor();
                    int rgb = swatch.getRgb();
                    tvTitle.setTextColor(titleTextColor);
                    tvTitle.setBackgroundColor(getTransColor(0.5f,rgb));
                }
                palette.getDarkVibrantSwatch();
            }
        });
    }

    /**
     * rgb为32位二进制数
     * 获取透明颜色值
     * @param percent
     * @param rgb
     */
    private int getTransColor(float percent,int rgb){
        int blue = rgb & 0xFF;//Color.blue(rgb)
        int green = (rgb >> 8) & 0xFF;//Color.green(rgb)
        int red = (rgb >> 16) & 0xFF;//Color.red(rgb)
        int alpha = rgb >>> 24;//Color.alpha(rgb)

        alpha = Math.round(alpha*percent);

        return Color.argb(alpha,red,green,blue);
    }

    protected int getTranslucentColor(float percent, int rgb) {
        int blue = Color.blue(rgb);
        int green = Color.green(rgb);
        int red = Color.red(rgb);
        int alpha = Color.alpha(rgb);
        alpha = Math.round(alpha * percent);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public RxPresenter setPresenter() {
        return null;
    }
}
