package com.example.passwordmanager.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.passwordmanager.R;
import com.example.passwordmanager.cryptage.Decrypt;
import com.example.passwordmanager.model.ApiHashes;
import com.example.passwordmanager.model.RetrofitInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class WebsiteData extends DialogFragment implements View.OnClickListener{

    private Button apicallbtn;
    private TextView passwordval;
    private String passwordvalue;

    private TextView urlTextView;
    private TextView emailTextView;
    private TextView passwordTextView;


    private Button seepass;

    private boolean pwned = FALSE;

    public WebsiteData() {

    }

    public static WebsiteData newInstance(String url, String email, String password) {
        WebsiteData fragment = new WebsiteData();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("email", email);
        args.putString("password", password);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.datavisualization, container, false);
        apicallbtn = (Button) view.findViewById(R.id.button4);
        apicallbtn.setOnClickListener(this);
        seepass = (Button) view.findViewById(R.id.seePassword);
        seepass.setOnClickListener(this);


        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        String url = bundle.getString("url");
        String email = bundle.getString("email");
        String passwordHashed = bundle.getString("password");


        urlTextView = (TextView) view.findViewById(R.id.webSiteUrl);
        urlTextView.setText(url);

        emailTextView = (TextView) view.findViewById(R.id.email);
        emailTextView.setText(email);

        passwordTextView = (TextView) view.findViewById(R.id.passwordClearOrNot);
        passwordTextView.setText(passwordHashed);

        //        access to cleared password
        passwordvalue = Decrypt.decrypt(passwordTextView.getText().toString().trim());



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }


    @Override
    public void onResume() {
        if(getDialog() != null) {
            //User able to dismiss dialog :
            //getDialog().setCancelable(false);

            Window window = getDialog().getWindow();
            Point size = new Point();

            // Store dimensions of the screen in `size`
            window.getWindowManager().getDefaultDisplay().getSize(size);

            // Set the width of the dialog to the dimensions of the screen
            window.setLayout((int) (0.75 * size.x), (int) (0.5 * size.y));
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(R.drawable.dialog_inset);
        }
        super.onResume();
    }

    private void init(View view) {


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.seePassword:
                 passwordTextView.setText(passwordvalue);
                 break;

            case R.id.button4:  String sha1 = "";
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");
                    digest.reset();
                    Log.d("tagPasswordOnClick", "onClick test Password: "+passwordvalue);
                    digest.update(passwordvalue.getBytes("utf8"));

                    sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
                } catch (Exception e){
                    e.printStackTrace();
                }
                String ffchars = sha1.substring(0, 5);
                RetrofitInstance.setPrefix(ffchars);
                RetrofitInterface retro = RetrofitInstance.getRetrofitInstance().create(RetrofitInterface.class);
                Call<List<ApiHashes>> listcall = retro.getallhashes();
                String finalSha = sha1;
                listcall.enqueue(new Callback<List<ApiHashes>>() {
                    @Override
                    public void onResponse(Call<List<ApiHashes>> call, Response<List<ApiHashes>> response) {
                        int l = response.body().size();
                        for(int i = 0 ; i < l ; i++){
                            if(finalSha.equals(response.body().get(i))){
                                pwned = TRUE;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ApiHashes>> call, Throwable t) {
                        Log.d("err","failed to perform api call to haveibeenpwned");
                    }
                });
                if(pwned){
                    apicallbtn.setText("you have been pwned");
                }else{
                    apicallbtn.setText("you are safe");
                }
                break;

        }
    }
}