package com.htdocscat.nomoreoverlay;

import android.os.IBinder;
import android.os.Parcel;
import de.robv.android.xposed.IXposedHookLoadPackage;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class TE implements IXposedHookLoadPackage{

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.android.providers.settings"))
            return;
		
		try{
			Class<?> classServiceMgr = XposedHelpers.findClass("android.os.ServiceManager", null);
			IBinder flinger = (IBinder) XposedHelpers.callStaticMethod(classServiceMgr, "getService", "SurfaceFlinger");
			if (flinger != null){
				Parcel data = Parcel.obtain();
				data.writeInterfaceToken("android.ui.ISurfaceComposer");
				data.writeInt(1);
				flinger.transact(1008, data, null, 0);
				data.recycle();
			}
		}catch(Exception e){
			XposedBridge.log(e);
		}
	}
}
