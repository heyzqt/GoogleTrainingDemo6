package com.heyzqt.googletrainingdemo6;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by heyzqt on 12/4/2017.
 */

public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener,
		LoaderManager.LoaderCallbacks<Cursor> {

	private ListView mListView;

	private CursorAdapter mCursorAdapter;

	private String[] from = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};

	private int[] to = {android.R.id.text1};

	private final static int COLUMN_CONTACT_ID = 0;
	private final static int COLUMN_CONTACT_KEY = 1;

	private static final String[] PROJECTION = {
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.LOOKUP_KEY,
			ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
	};

	//private String SELECTION = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
	private String SELECTION = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
	private String mSearchString;
	private String[] mSelectionArgs = {mSearchString};

	private static final String TAG = "zhangqing";

	public ContactsFragment() {
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.contacts_list_fragment, container, false);
	}

	@SuppressLint("ResourceType")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView = (ListView) getActivity().findViewById(android.R.id.list);

		mCursorAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.contacts_list_item,
				null,
				from,
				to,
				0);

		mListView.setAdapter(mCursorAdapter);
		mListView.setOnItemClickListener(this);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CursorAdapter cursorAdapter = (CursorAdapter) parent.getAdapter();
		Cursor cursor = cursorAdapter.getCursor();
		cursor.moveToPosition(position);

		Long contactId = cursor.getLong(COLUMN_CONTACT_ID);
		String contactKey = cursor.getString(COLUMN_CONTACT_KEY);
		Uri contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey);
		Log.i(TAG, "onItemClick: contact uri = " + contactUri);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mSearchString = "1840824";
		mSelectionArgs[0] = "%" + mSearchString + "%";
		return new CursorLoader(getActivity(),
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				PROJECTION,
				SELECTION,
				mSelectionArgs,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mCursorAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);
	}
}
