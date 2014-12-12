package com.myaddressbook.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.dynamicgrid.DynamicGridView;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.MaterialEditText;
import com.gc.materialdesign.widgets.Dialog;
import com.myaddressbook.Activities.ActCreatePeople;

import com.myaddressbook.Activities.ActPeopleList;
import com.myaddressbook.Activities.ActSecond;
import com.myaddressbook.Cheeses;
import com.myaddressbook.R;
import com.myaddressbook.adapter.CheeseDynamicAdapter;
import com.myaddressbook.adapter.GroupAdapter;
import com.myaddressbook.app.AppController;
import com.myaddressbook.util.DaoManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    private static final String TAG = GroupFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;
    private GridView mGridView;
    private ButtonRectangle mBtn_newpeople;
    private ButtonRectangle mBtn_newgroup;
    private List<AddressBook> addressBookArrayList;
    private GroupAdapter groupAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupFragment() {
        // Required empty public constructor
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //GET 第一層 DATA
        addressBookArrayList = AppController.getInstance().getDaofManger().getAddressBookList(1, "");
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mBtn_newpeople = (ButtonRectangle) view.findViewById(R.id.btn_new);
        mBtn_newgroup = (ButtonRectangle) view.findViewById(R.id.btn_group);

        groupAdapter = new GroupAdapter(getActivity(), addressBookArrayList);

        mGridView.setAdapter(groupAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBook addressBook = addressBookArrayList.get(position);
                //TODO:未分類
                if (addressBook.getPeopleNo().equals("1000000000")) {
                    Intent intent = new Intent(getActivity(), ActPeopleList.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Level", 1);
                    intent.putExtra("ParentNo", "1000000000");
                    intent.putExtra("ParentName", "未分類");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }

                //TODO:判斷下一層是否有資料,前往不同的ACTIVITY
                List<AddressBook> addressBookList = AppController.getInstance().getDaofManger().getAddressBookList(2, addressBook.getPeopleNo());
                if (addressBookList.size() > 1) {
                    //下一層級
                    Intent secondIntent = new Intent();
                    secondIntent.setClass(getActivity(), ActSecond.class);
                    secondIntent.putExtra("ParentNo", addressBook.getPeopleNo());
                    secondIntent.putExtra("ParentName", addressBook.getPeopleName());
                    startActivity(secondIntent);
                } else {
                    //名單頁
                    Intent people = new Intent();
                    people.setClass(getActivity(), ActPeopleList.class);
                    people.putExtra("ParentNo", addressBook.getPeopleNo());
                    people.putExtra("ParentName", addressBook.getPeopleName());
                    Bundle bundle = new Bundle();
                    bundle.putInt("Level", 1);
                    people.putExtras(bundle);
                    startActivity(people);
                }

            }
        });
        //新增聯絡人
        mBtn_newpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ActCreatePeople.class);
                Bundle bundle = new Bundle();
                //傳目前所在的層級
                bundle.putInt("Level", 1);

                intent.putExtra("ParentNo", "1000000000");
                intent.putExtra("ParentName", "未分類");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //新增群組
        mBtn_newgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
                editDialog.setTitle(R.string.btn_new_group);

                final MaterialEditText editText = new MaterialEditText(getActivity());
                //final EditText editText = new EditText(getActivity());
                editText.setHint(R.string.edit_hint_text);
                editText.setSingleLineEllipsis(true);
                editText.setMaxCharacters(10);
                editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

                editText.setBaseColor(getResources().getColor(R.color.base_color));
                editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
                editText.setErrorColor(getResources().getColor(R.color.error_color));
                editText.setPaddings(30, 30, 30, 30);
                editText.setTextSize(18);
                editDialog.setView(editText);

                editDialog.setPositiveButton(R.string.alert_submit, new DialogInterface.OnClickListener() {
                    // insert group to db
                    public void onClick(DialogInterface arg0, int arg1) {
                        String groupname = editText.getText().toString().trim();
                        if (groupname.length() > 10) {
                            Toast.makeText(getActivity(), R.string.toast_edit_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean isSuccess = AppController.getInstance().getDaofManger().InsertAdd(groupname, 1, "");
                        if (isSuccess) {
                            addressBookArrayList.clear();
                            addressBookArrayList.addAll(AppController.getInstance().getDaofManger().getAddressBookList(1, ""));
                            groupAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), R.string.toast_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), R.string.toast_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                editDialog.setNegativeButton(R.string.alert_cancal, new DialogInterface.OnClickListener() {
                    // cancel
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                editDialog.show();


            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
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
