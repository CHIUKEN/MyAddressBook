package com.myaddressbook.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.daogenerator.Tag;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.MaterialEditText;
import com.myaddressbook.Activities.ActHome;
import com.myaddressbook.Activities.ActTagPeopleList;
import com.myaddressbook.R;
import com.myaddressbook.adapter.TagAdapter;
import com.myaddressbook.app.AppController;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TagFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number2";


    private GridView mGridView;
    private ButtonFloat mBtn_newpeople;
    private ButtonRectangle mBtn_newgroup;
    private List<Tag> tagList;
    private TagAdapter tagAdapter;
//    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static TagFragment newInstance(int sectionNumber) {
        TagFragment fragment = new TagFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }

    public TagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview);
        mBtn_newgroup = (ButtonRectangle) view.findViewById(R.id.btn_tag);
        tagList = AppController.getInstance().getDaofManger().getAllTag();
        tagAdapter = new TagAdapter(getActivity(), tagList);
        mGridView.setAdapter(tagAdapter);
        //新增標籤
        mBtn_newgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MaterialDialog editDialog = new MaterialDialog(getActivity())
                        .setTitle(R.string.btn_new_tag);

                final MaterialEditText editText = new MaterialEditText(getActivity());

                editText.setHint(R.string.edit_hint_text);
                editText.setSingleLineEllipsis(true);
                editText.setMaxCharacters(10);
                editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

                editText.setBaseColor(getResources().getColor(R.color.base_color));
                editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
                editText.setErrorColor(getResources().getColor(R.color.error_color));

                editText.setTextSize(18);
                editDialog.setContentView(editText);
                //新增標籤
                editDialog.setPositiveButton(R.string.alert_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tagname = editText.getText().toString().trim();
                        if (tagname.length() > 10) {
                            Toast.makeText(getActivity(), R.string.toast_edit_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean isSuccess = AppController.getInstance().getDaofManger().insertTag(tagname);
                        if (isSuccess) {
                            tagList.clear();
                            tagList.addAll(AppController.getInstance().getDaofManger().getAllTag());
                            tagAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), R.string.toast_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                        }
                        editDialog.dismiss();
                    }
                });
                editDialog.setNegativeButton(R.string.alert_cancal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                editDialog.show();

            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tag tag = tagList.get(i);
                Intent intent = new Intent(getActivity(), ActTagPeopleList.class);
                intent.putExtra("TagId", tag.getTagId());
                startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((ActHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
