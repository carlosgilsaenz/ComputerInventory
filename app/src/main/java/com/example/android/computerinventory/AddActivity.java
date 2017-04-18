package com.example.android.computerinventory;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @BindView(R.id.image_view)
    ImageView mComputerImage;

    @OnClick({R.id.radio_button_desktop, R.id.radio_button_laptop, R.id.radio_button_tablet})
    public void updateComputerImage() {
        int checkedButton = mRadioGroup.getCheckedRadioButtonId();

        switch (checkedButton){
            case R.id.radio_button_desktop:
                mComputerImage.setImageResource(R.drawable.ic_desktop);
                break;
            case R.id.radio_button_laptop:
                mComputerImage.setImageResource(R.drawable.ic_laptop);
                break;
            case R.id.radio_button_tablet:
                mComputerImage.setImageResource(R.drawable.ic_tablet);
                break;
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ButterKnife.bind(this);
    }
}
