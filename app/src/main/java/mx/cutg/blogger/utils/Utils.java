package mx.cutg.blogger.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.View;

public class Utils
{
    @SuppressWarnings("UnusedDeclaration")
	public static int dpToPx(float dp, Resources resources)
    {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}


    @SuppressWarnings("UnusedDeclaration")
	public static float dipOrDpToFloat(String value)
    {
		if (value.contains("dp")){
			value = value.replace("dp", "");
		}
		else{
			value = value.replace("dip", "");
		}

		return Float.parseFloat(value);
	}


    @SuppressWarnings("UnusedDeclaration")
	public static int getRelativeTop(View myView)
    {
	    if(myView.getId() == android.R.id.content){
            return myView.getTop();
        }
	    else{
            return myView.getTop() + getRelativeTop((View) myView.getParent());
        }
	}


    @SuppressWarnings("UnusedDeclaration")
	public static int getRelativeLeft(View myView)
    {
		if(myView.getId() == android.R.id.content){
            return myView.getLeft();
        }
		else{
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
        }
	}


    @SuppressWarnings("UnusedDeclaration")
    public static Typeface getFont(Context ctx, int op)
    {
        Typeface font;

        switch(op)
        {
            case 0: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Thin.ttf"); break;
            case 1: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Light.ttf"); break;
            case 2: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Regular.ttf"); break;
            case 3: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Medium.ttf"); break;
            case 4: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Bold.ttf"); break;
            case 5: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Black.ttf"); break;
            default: font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Medium.ttf"); break;
        }

        return font;
    }


    @SuppressWarnings("UnusedDeclaration")
    //METODO QUE VALIDA CONEXION A INTERNET
    public static boolean hadInternet(Activity act)
    {
        try
        {
            ConnectivityManager connMgr = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi   = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            return wifi.isConnected() || mobile.isConnected();
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
