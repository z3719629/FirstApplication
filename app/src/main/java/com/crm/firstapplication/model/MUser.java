package com.crm.firstapplication.model;

import android.databinding.ObservableField;

/**
 * Created by Administrator on 2018/1/2.
 */
public class MUser {
    public final ObservableField<String> userTitle = new ObservableField<>();
    public final ObservableField<String> userName = new ObservableField<>();
    public final ObservableField<String> userPassword = new ObservableField<>();
    //public final ObservableField<Drawable> headPic = new ObservableField<>();
    public final ObservableField<String> loginButtonName = new ObservableField<>();
}
