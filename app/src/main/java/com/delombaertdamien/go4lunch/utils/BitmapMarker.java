package com.delombaertdamien.go4lunch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.delombaertdamien.go4lunch.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Create Not By Damien De Lombaert - Reference unknown
 * 2020
 */
public class BitmapMarker {

    public static BitmapDescriptor getBitmapDescriptorFromResourceDrawable(Context context, @DrawableRes int resDrawableVector, @DrawableRes int resDrawableBackground){
        Drawable background = ContextCompat.getDrawable(context, resDrawableBackground);
        background.setBounds(0,0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectDrawable = ContextCompat.getDrawable(context, resDrawableVector);
        vectDrawable.setBounds(0,10, vectDrawable.getIntrinsicWidth() - 10, vectDrawable.getIntrinsicHeight() - 10 );
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
