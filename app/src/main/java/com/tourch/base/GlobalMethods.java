package com.tourch.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.tourch.R;
import com.tourch.ui.rider.SearchAreaFilterAdapter;
import com.tourch.ui.rider.SelectFiltersAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class GlobalMethods {

    public static DialogListener listner = null;
    public static MySelfDialogListener mySelfDialogListner = null;
    public static PermissionDialogListener permissionDialogListner = null;
    public static scheduleRideDialogListener scheduleRideDialogListener = null;
    public static FilterRideDialogListener filterRideDialogListener = null;
    public static Dialog dialog;

    public static void Dialog(Context context) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                GlobalMethods.dialog.dismiss();
            }
            //return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView btnDontAllow = dialog.findViewById(R.id.btnDontAllow);
        TextView btnAllow = dialog.findViewById(R.id.btnAllow);


        btnDontAllow.setOnClickListener(v -> {

            dialog.dismiss();
            if (listner == null)
                return;
            else {
                listner.setCancelClick();
                listner = null;
            }

        });
        btnAllow.setOnClickListener(v -> {

            dialog.dismiss();
            if (listner == null)
                return;
            else {
                listner.setOkClick();
                listner = null;
            }

        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();

    }


    public static void mySelfDialog(Context context, MySelfDialogListener mySelfDialog) {

        mySelfDialogListner = mySelfDialog;
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_ride_for);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RelativeLayout rlAddUser = dialog.findViewById(R.id.rlAddUser);
        // TextView btnAllow = dialog.findViewById(R.id.btnAllow);


        rlAddUser.setOnClickListener(v -> {
            dialog.dismiss();
            mySelfDialogListner.setAddRiderClick();

        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM;
        wlp.y = 100;
        window.setAttributes(wlp);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();

    }


    public static void contactPermissionDialog(Activity context, PermissionDialogListener permissionDialog) {
        try {
            permissionDialogListner = permissionDialog;
        } catch (ClassCastException e) {

        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contact_permission);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvTurnOn = dialog.findViewById(R.id.tvTurnOn);
        TextView tvTurnOff = dialog.findViewById(R.id.tvTurnOff);


        tvTurnOn.setOnClickListener(v -> {
            dialog.dismiss();


            permissionDialogListner.setOkClick();


        });

        tvTurnOff.setOnClickListener(v -> {
            dialog.dismiss();

            permissionDialogListner.setCancelClick();


        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.y = 100;
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();

    }


    public static void scheduleRideDialog(Context context, scheduleRideDialogListener scheduleRideDialog) {
        try {
            scheduleRideDialogListener = scheduleRideDialog;
        } catch (ClassCastException e) {

        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ride_schedule);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextInputEditText edDate = dialog.findViewById(R.id.edDate);
        LinearLayout llTime = dialog.findViewById(R.id.llTime);
        TextView tvDone = dialog.findViewById(R.id.tvDone);
        TextView tvTimeSchedule = dialog.findViewById(R.id.tvTimeSchedule);
        RadioGroup rgFilter = dialog.findViewById(R.id.rgFilter);
        RadioButton rbAm = dialog.findViewById(R.id.rbAm);
        RadioButton rbPm = dialog.findViewById(R.id.rbPm);
       // CalendarView cvDate = dialog.findViewById(R.id.cvDate);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String currentDate = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        //cvDate.setMinimumDate(calendar);

        tvTimeSchedule.setText(currentDate);
        edDate.setOnClickListener(v -> {
            scheduleRideDialogListener.setDateClick();
        });


        tvDone.setOnClickListener(v -> {
            dialog.dismiss();
            scheduleRideDialogListener.setDoneClick();

        });

        llTime.setOnClickListener(v -> {
            scheduleRideDialogListener.setTimeClick();
        });

        rgFilter.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = dialog.findViewById(i);
            if (rb.getText().equals("AM")) {
                rbAm.setTextColor(Color.WHITE);
                rbPm.setTextColor(Color.BLACK);
                scheduleRideDialogListener.setTimeAmPmClick("AM");
            } else {
                rbAm.setTextColor(Color.BLACK);
                rbPm.setTextColor(Color.WHITE);
                scheduleRideDialogListener.setTimeAmPmClick("PM");
            }
        });

/*        cvDate.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();

            scheduleRideDialogListener.setCalenderClick(clickedDayCalendar);
        });*/


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.y = 100;
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();

    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    public static void filterDialog(Context context, ArrayList<String> area, FilterRideDialogListener filterRideDialog) {
        ArrayList<String> selectFiltersModelArrayList = new ArrayList<>();

        try {
            filterRideDialogListener = filterRideDialog;
        } catch (ClassCastException e) {

        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            //return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvDone = dialog.findViewById(R.id.tvDone);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        RadioGroup rgPassenger = dialog.findViewById(R.id.rgPassenger);
        RadioButton rbOne = dialog.findViewById(R.id.rbOne);
        RadioButton rbTwo = dialog.findViewById(R.id.rbTwo);
        RadioButton rbThree = dialog.findViewById(R.id.rbThree);
        RadioButton rbFour = dialog.findViewById(R.id.rbFour);
        RadioButton rbCustom = dialog.findViewById(R.id.rbCustom);
        GridView gvSearchArea = dialog.findViewById(R.id.gvSearchArea);
        RecyclerView rvFilters = dialog.findViewById(R.id.rvFilters);
        RangeSlider rsPrice = dialog.findViewById(R.id.rsPrice);


        // RangeSlider s = new RangeSlider(context);
        rsPrice.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                int val = (int) value;
                return "$" + val;
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvFilters.setLayoutManager(staggeredGridLayoutManager);
        SelectFiltersAdapter selectFiltersAdapter = new SelectFiltersAdapter(selectFiltersModelArrayList, new SelectFiltersAdapter.OnFilterClick() {
            @Override
            public void onDelete(int position) {

            }
        });
        rvFilters.setAdapter(selectFiltersAdapter);

        SearchAreaFilterAdapter searchAreaFilterAdapter = new SearchAreaFilterAdapter(context, area, (position, area1) -> {
            if (selectFiltersModelArrayList.size() != 0) {
                for (int k = 0; k < selectFiltersModelArrayList.size(); k++) {
                    if (selectFiltersModelArrayList.get(k).contains("mile")) {
                        selectFiltersModelArrayList.set(k, area1);
                    } else {
                        selectFiltersModelArrayList.add(area1);
                    }
                }
            } else {
                selectFiltersModelArrayList.add(area1);
            }

            selectFiltersAdapter.updateList(selectFiltersModelArrayList);
            filterRideDialogListener.setSearchAreaClick(area1);
        });
        gvSearchArea.setAdapter(searchAreaFilterAdapter);

        tvDone.setOnClickListener(v -> {
            dialog.dismiss();
            filterRideDialogListener.setDoneClick();

        });

        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
            filterRideDialogListener.setDoneClick();

        });

        rgPassenger.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = dialog.findViewById(i);
            if (rb.getText().equals("1")) {
                rbOne.setTextColor(Color.WHITE);
                rbOne.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
                rbTwo.setTextColor(Color.BLACK);
                rbTwo.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbThree.setTextColor(Color.BLACK);
                rbThree.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbFour.setTextColor(Color.BLACK);
                rbFour.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbCustom.setTextColor(Color.BLACK);
                rbCustom.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));

                if (selectFiltersModelArrayList.size() != 0) {
                    for (int k = 0; k < selectFiltersModelArrayList.size(); k++) {
                        if (selectFiltersModelArrayList.get(k).contains("seat")) {
                            selectFiltersModelArrayList.set(k, "1 seats");
                        } else {
                            selectFiltersModelArrayList.add("1 seat");
                        }
                    }
                } else {
                    selectFiltersModelArrayList.add("1 seat");
                }

                selectFiltersAdapter.updateList(selectFiltersModelArrayList);

                filterRideDialogListener.setPassengerClick("1");
            } else if (rb.getText().equals("2")) {
                rbOne.setTextColor(Color.BLACK);
                rbOne.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbTwo.setTextColor(Color.WHITE);
                rbTwo.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
                rbThree.setTextColor(Color.BLACK);
                rbThree.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbFour.setTextColor(Color.BLACK);
                rbFour.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbCustom.setTextColor(Color.BLACK);
                rbCustom.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                filterRideDialogListener.setPassengerClick("2");
                if (selectFiltersModelArrayList.size() != 0) {
                    for (int k = 0; k < selectFiltersModelArrayList.size(); k++) {
                        if (selectFiltersModelArrayList.get(k).contains("seat")) {
                            selectFiltersModelArrayList.set(k, "2 seats");
                        } else {
                            selectFiltersModelArrayList.add("2 seats");
                        }
                    }
                } else {
                    selectFiltersModelArrayList.add("2 seats");
                }

                selectFiltersAdapter.updateList(selectFiltersModelArrayList);
            } else if (rb.getText().equals("3")) {
                rbOne.setTextColor(Color.BLACK);
                rbOne.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbTwo.setTextColor(Color.BLACK);
                rbTwo.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbThree.setTextColor(Color.WHITE);
                rbThree.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
                rbFour.setTextColor(Color.BLACK);
                rbFour.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbCustom.setTextColor(Color.BLACK);
                rbCustom.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                filterRideDialogListener.setPassengerClick("3");
                if (selectFiltersModelArrayList.size() != 0) {
                    for (int k = 0; k < selectFiltersModelArrayList.size(); k++) {
                        if (selectFiltersModelArrayList.get(k).contains("seat")) {
                            selectFiltersModelArrayList.set(k, "3 seats");
                        } else {
                            selectFiltersModelArrayList.add("3 seats");
                        }
                    }
                } else {
                    selectFiltersModelArrayList.add("3 seats");
                }

                selectFiltersAdapter.updateList(selectFiltersModelArrayList);
            } else if (rb.getText().equals("4")) {
                rbOne.setTextColor(Color.BLACK);
                rbOne.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbTwo.setTextColor(Color.BLACK);
                rbTwo.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbThree.setTextColor(Color.BLACK);
                rbThree.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbFour.setTextColor(Color.WHITE);
                rbFour.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
                rbCustom.setTextColor(Color.BLACK);
                rbCustom.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                filterRideDialogListener.setPassengerClick("4");

                if (selectFiltersModelArrayList.size() != 0) {
                    for (int k = 0; k < selectFiltersModelArrayList.size(); k++) {
                        if (selectFiltersModelArrayList.get(k).contains("seat")) {
                            selectFiltersModelArrayList.set(k, "4 seats");
                        } else {
                            selectFiltersModelArrayList.add("4 seats");
                        }
                    }
                } else {
                    selectFiltersModelArrayList.add("4 seats");
                }

                selectFiltersAdapter.updateList(selectFiltersModelArrayList);
            } else if (rb.getText().equals("Custom")) {
                rbOne.setTextColor(Color.BLACK);
                rbOne.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbTwo.setTextColor(Color.BLACK);
                rbTwo.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbThree.setTextColor(Color.BLACK);
                rbThree.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbFour.setTextColor(Color.BLACK);
                rbFour.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.black)));
                rbCustom.setTextColor(Color.WHITE);
                rbCustom.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.white)));

               /* if (selectFiltersModelArrayList.size()!=0) {
                    for (int k = 0; k < selectFiltersModelArrayList.size(); k++) {
                        if (selectFiltersModelArrayList.get(k).contains("seat")) {
                            selectFiltersModelArrayList.set(k, "custom seats");
                        }else {
                            selectFiltersModelArrayList.add("custom seats");
                        }
                    }
                }else {
                    selectFiltersModelArrayList.add("custom seats");
                }


                selectFiltersAdapter.updateList(selectFiltersModelArrayList);*/
                filterRideDialogListener.setPassengerClick("custom");
            }
        });


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.y = 100;
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();

    }


    public static void loadImage(Context context, String url, ImageView imageView, int placeholder, int errorImage) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                // .override(512, 512)
                .error(errorImage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {
                        Log.e("ErrrorGlide", "ErrrorGlide==" + e.getCauses().toString());
                        return false; // important to return false so the error placeholder can be placed

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {

                        return false;

                    }
                })
                .into(imageView);
    }

    public static String getFormatedDateTime(String dateStr, String strReadFormat, String strWriteFormat) {

        String formattedDate = dateStr;

        DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.ENGLISH);
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.ENGLISH);

        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String changeDateForDate(String date) {
        String outputDate = "";
        if (date != null && !date.equalsIgnoreCase("")) {
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date oneWayTripDate = input.parse(date);
                // parse input
                //Crashlytics.logException(new Throwable("this is issue:"+oneWayTripDate.toString()));
                Log.e("", "datenewinfunction : " + date.toString());// format output
                outputDate = output.format(oneWayTripDate);
            } catch (ParseException e) {


                outputDate = date;
                e.printStackTrace();
            }
        }

        return outputDate;
    }


    public static String changeDateFormatTOYYYYMMDD(String date) {
        String outputDate = null;
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date oneWayTripDate = input.parse(date);// parse input
            //Crashlytics.logException(new Throwable("this is issue:"+oneWayTripDate.toString()));
            Log.e("", "datenewinfunction : " + date.toString());// format output
            outputDate = output.format(oneWayTripDate);
        } catch (ParseException e) {


            outputDate = date;

            e.printStackTrace();
        }
        return outputDate;
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public interface DialogListener {
        void setOkClick();

        void setCancelClick();
    }

    public interface MySelfDialogListener {
        void setAddRiderClick();

        void setMySelfClick();

    }

    public interface PermissionDialogListener {
        void setOkClick();

        void setCancelClick();
    }

    public interface scheduleRideDialogListener {
        void setDateClick();

        void setTimeClick();

        void setTimeAmPmClick(String amPm);

        void setCalenderClick(Calendar calenderClick);

        void setDoneClick();

    }

    public interface FilterRideDialogListener {
        void setPriceClick(String priceFrom, String priceTo);

        void setPassengerClick(String passenger);

        void setSearchAreaClick(String searchArea);

        void setDoneClick();

        void setCloseClick();
    }

    public static boolean isInternetAvailable(Context ctx) {

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if network is NOT available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            //GlobalMethods.Dialog(ctx,"Please check your internet connection and try again");
            return false;
        }


        // Toast.makeText(ctx,Utility.getStringSharedPreferences(ctx,StringDifferentLanguage.NO_INTERNET_CONNECTION),Toast.LENGTH_SHORT).show();
        //Toast.makeText(ctx,"No internet connection",Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void shareKit(Context context, String shareBodyText, String subject, String SharingOption, String uri) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        context.startActivity(Intent.createChooser(sharingIntent, SharingOption));

    }

    public static void Dialog(Context context, String msg, String btnMessage) {

        if (dialog != null) {
            if (dialog.isShowing()) {

                return;
            }
            //return;
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);

        txt_dialog_msg.setText(msg);
        txt_dialog_ok.setText(btnMessage);

       /* txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProTextRegular(context));
        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProTextSemibold(context));*/

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (listner == null)
                    return;
                else {
                    listner.setOkClick();
                    listner = null;
                }


            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();


    }


}
