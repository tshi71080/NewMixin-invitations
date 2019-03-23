package com.liuniukeji.mixin.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.pub.note.PubNotesActivity;
import com.liuniukeji.mixin.ui.pub.video.PubVideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 发布页面
 */
public class PubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.pub_pic_note_ly)
    LinearLayout pubPicNoteLy;
    @BindView(R.id.pub_video_note_ly)
    LinearLayout pubVideoNoteLy;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    public static PubFragment newInstance(String param1, String param2) {
        PubFragment fragment = new PubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pub, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.pub_pic_note_ly, R.id.pub_video_note_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pub_pic_note_ly:
                //发布图文
                 startActivity(new Intent().setClass(getActivity(), PubNotesActivity.class));
                break;
            case R.id.pub_video_note_ly:
                //发布视频
                startActivity(new Intent().setClass(getActivity(), PubVideoActivity.class));
                break;
            default:
                break;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
