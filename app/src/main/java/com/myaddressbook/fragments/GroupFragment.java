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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.daogenerator.AddressBook;
import com.dynamicgrid.DynamicGridView;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.MaterialEditText;
import com.gc.materialdesign.widgets.Dialog;
import com.myaddressbook.Activities.ActCreatePeople;

import com.myaddressbook.Activities.ActHome;
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

import me.drakeet.materialdialog.MaterialDialog;

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

    //private OnFragmentInteractionListener mListener;
    private GridView mGridView;
    private ButtonFloat mBtn_newpeople;
    private ButtonRectangle mBtn_newgroup;
    private List<AddressBook> addressBookArrayList;
    private GroupAdapter groupAdapter;

    private MaterialDialog mMaterialDialog;
    private static final String ARG_SECTION_NUMBER = "section_number1";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(int sectionNumber) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMaterialDialog = new MaterialDialog(getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //GET 第一層 DATA
        addressBookArrayList = AppController.getInstance().getDaofManger().getAddressBookList(1, "");
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mBtn_newpeople = (ButtonFloat) view.findViewById(R.id.btn_float_newpeople);
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
        //長按更改名稱
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final AddressBook addressBook = addressBookArrayList.get(position);
                //TODO:CHANTE GROUP NAME
                final MaterialDialog editDialog = new MaterialDialog(getActivity())
                        .setTitle(R.string.dialog_change_groupname);

                final MaterialEditText editText = new MaterialEditText(getActivity());

                editText.setHint(R.string.edit_hint_text);
                editText.setSingleLineEllipsis(true);
                editText.setMaxCharacters(10);
                editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

                editText.setBaseColor(getResources().getColor(R.color.base_color));
                editText.setPrimaryColor(getResources().getColor(R.color.primaryColor));
                editText.setErrorColor(getResources().getColor(R.color.error_color));

                editText.setTextSize(18);
                editText.setText(addressBook.getPeopleName());
                editText.setSelection(addressBook.getPeopleName().length());
                editDialog.setContentView(editText);

                editDialog.setPositiveButton(R.string.alert_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO:UPDATE NAME
                        addressBook.setPeopleName(editText.getText().toString());
                        AppController.getInstance().getDaofManger().updateSingleAddressbook(addressBook);
                        groupAdapter.notifyDataSetChanged();
                        editDialog.dismiss();
                    }
                });

                editDialog.setNegativeButton(R.string.alert_cancal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editDialog.dismiss();
                    }
                });

                editDialog.show();
                return true;
            }
        });
        //QuickReturn button
        int footerHeight = getResources().getDimensionPixelSize(R.dimen.flatButton_bottom);
        footerHeight += getResources().getDimensionPixelSize(R.dimen.flatButton_height);
        mGridView.setOnScrollListener(new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, mBtn_newpeople, footerHeight));

        //新增聯絡人
        mBtn_newpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                final MaterialDialog editDialog = new MaterialDialog(getActivity())
                        .setTitle(R.string.btn_new_group);

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

                editDialog.setPositiveButton(R.string.alert_submit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        editDialog.dismiss();
                    }
                });

                editDialog.setNegativeButton(R.string.alert_cancal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editDialog.dismiss();
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
            ((ActHome) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
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
