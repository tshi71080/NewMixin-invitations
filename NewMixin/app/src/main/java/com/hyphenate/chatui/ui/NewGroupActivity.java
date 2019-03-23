/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.chatui.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager.EMGroupStyle;
import com.hyphenate.chat.EMGroupOptions;
import com.liuniukeji.mixin.R;
import com.hyphenate.exceptions.HyphenateException;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 新建群组
 */

public class NewGroupActivity extends BaseActivity implements NewGroupContract.View {
    private EditText groupNameEditText;
    private ProgressDialog progressDialog;
    private EditText introductionEditText;
    private CheckBox publibCheckBox;
    private CheckBox memberCheckbox;
    private TextView secondTextView;

    NewGroupContract.Presenter presenter;

    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_new_group);

        presenter = new NewGroupPresenter(this);
        presenter.attachView(this);

        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
        publibCheckBox = (CheckBox) findViewById(R.id.cb_public);
        memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
        secondTextView = (TextView) findViewById(R.id.second_desc);

        publibCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondTextView.setText(R.string.join_need_owner_approval);
                } else {
                    secondTextView.setText(R.string.Open_group_members_invited);
                }
            }
        });
    }

    /**
     * @param v
     */
    public void save(View v) {
        String name = groupNameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            //new EaseAlertDialog(this, R.string.Group_name_cannot_be_empty).show();

            ToastUtils.showShortToast("群组名称不能为空");
        } else {
            // select from contact list
//            startActivityForResult(new Intent(this,
//                    GroupPickContactsActivity.class).putExtra("groupName", name), 0);

            createGroup();
        }
    }


    /**
     * 创建群组
     */
    @SuppressWarnings("all")
    private void createGroup() {
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final String groupName = groupNameEditText.getText().toString().trim();
                String desc = introductionEditText.getText().toString();
                String[] members = new String[0];
                try {
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    option.inviteNeedConfirm = true;

                    String reason = NewGroupActivity.this.getString(R.string.invite_join_group);
                    reason = EMClient.getInstance().getCurrentUser() + reason + groupName;

                    if (publibCheckBox.isChecked()) {
                        option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePublicJoinNeedApproval : EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePrivateMemberCanInvite : EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }

                    //环信新建群组之后
                    // EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);
                    EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);

                    groupId = group.getGroupId();

                    handler.sendEmptyMessage(1);


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.dismiss();
//                            setResult(RESULT_OK);
//                            finish();
//                        }
//                    });


                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    final String groupName = groupNameEditText.getText().toString().trim();
                    String desc = introductionEditText.getText().toString();
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.em_group_icon);
                    File logo = saveBitmapFile(bmp, Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");

                    if (EmptyUtils.isNotEmpty(groupId)) {
                        presenter.createGroup(groupId, groupName, desc, logo, "0", "0");

                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 把batmap 转file
     *
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        //将要保存图片的路径
        File file = new File(filepath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    @SuppressWarnings("all")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);
        if (resultCode == RESULT_OK) {
            //new group
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(st1);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String groupName = groupNameEditText.getText().toString().trim();
                    String desc = introductionEditText.getText().toString();
                    String[] members = data.getStringArrayExtra("newmembers");
                    try {
                        EMGroupOptions option = new EMGroupOptions();
                        option.maxUsers = 200;
                        option.inviteNeedConfirm = true;

                        String reason = NewGroupActivity.this.getString(R.string.invite_join_group);
                        reason = EMClient.getInstance().getCurrentUser() + reason + groupName;

                        if (publibCheckBox.isChecked()) {
                            option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePublicJoinNeedApproval : EMGroupStyle.EMGroupStylePublicOpenJoin;
                        } else {
                            option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePrivateMemberCanInvite : EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                        }

                        //环信新建群组之后
                        EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);

                        String groupId = group.getGroupId();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }

    @Override
    public void back(View view) {
        finish();
    }

    @Override
    public void liftData(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
        }

        finish();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
