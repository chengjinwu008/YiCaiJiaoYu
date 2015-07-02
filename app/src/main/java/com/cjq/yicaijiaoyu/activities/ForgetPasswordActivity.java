package com.cjq.yicaijiaoyu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cjq.yicaijiaoyu.R;

/**
 * Created by CJQ on 2015/6/29.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_layout);

        phoneNum = (EditText) findViewById(R.id.phone_num);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                //验证手机号码
                final String num = phoneNum.getText().toString();

//                Validator.verify(num, new Validator.VerifyListener() {
//                    @Override
//                    public void verifyFinished(boolean state) {
//                        if (state) {
//                            //验证成功
//                            Intent intent = new Intent(ForgetPasswordActivity.this, RegisterSMSActivity.class);
//                            intent.putExtra(ForgetPasswordSMSActivity.EXTRA_PHONE_NUM, num);
//                            startActivityForResult(intent, 0);
//                        } else {
//                            //验证失败
//                            Toast.makeText(ForgetPasswordActivity.this, R.string.wrong_num, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                Intent intent = new Intent(ForgetPasswordActivity.this, ForgetPasswordSMSActivity.class);
                intent.putExtra(ForgetPasswordSMSActivity.EXTRA_PHONE_NUM, num);
                startActivityForResult(intent, 0);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode==RESULT_OK){
                    setResult(RESULT_OK);
                    this.finish();
                }
                break;
        }
    }
}
