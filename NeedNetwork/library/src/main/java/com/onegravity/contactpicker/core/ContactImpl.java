/*
 * Copyright (C) 2015-2017 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegravity.contactpicker.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.onegravity.contactpicker.Helper;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactSortOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ContactImpl is the concrete Contact implementation.
 * It can be instantiated and modified only within its own package to prevent modifications from
 * classes outside the package.
 */
@Entity
public class ContactImpl extends ContactElementImpl implements Contact {

    private static final Pattern CONTACT_LETTER = Pattern.compile("[^a-zA-Z]*([a-zA-Z]).*");

    /**
     * @see <a href="http://www.google.com/design/spec/style/color.html#color-color-palette">Color palette used</a>
     */
    private final static int CONTACT_COLORS_MATERIAL[] = {
            0xffF44336,
            0xffE91E63,
            0xff9C27B0,
            0xff673AB7,
            0xff3F51B5,
            0xff2196F3,
            0xff03A9F4,
            0xff00BCD4,
            0xff009688,
            0xff4CAF50,
            0xff8BC34A,
            0xffCDDC39,
            0xffFFC107,
            0xffFF9800,
            0xffFF5722,
            0xff795548,
            0xff9E9E9E,
            0xff607D8B
    };

    public static ContactImpl fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        String[] names = displayName != null ? displayName.split("\\s+") : new String[]{"---", "---"};
        String firstName = names.length >= 1 ? names[0] : displayName;
        String lastName = names.length >= 2 ? names[1] : "";
        String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
        Uri uri = photoUri != null ? Uri.parse(photoUri) : null;
        return new ContactImpl(id, lookupKey, displayName, firstName, lastName, uri);
    }

    public static ContactImpl fromContact(Contact contact, int id) {
        String lookupKey = contact.getLookupKey();
        String displayName = contact.getDisplayName();
        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();
        Uri uri = contact.getPhotoUri();
        return new ContactImpl(id, lookupKey, displayName, firstName, lastName, uri);
    }

//    @PrimaryKey
//    public int uid;
    @ColumnInfo(name = "lookup_key")
    public String mLookupKey;
    private String mFirstName = "";
    private String mLastName = "";
    @ColumnInfo(name = "type")
    private int mType;
    @ColumnInfo(name = "phone_number")
    private String mPhoneNumber = "";
    @Ignore
    private Map<Integer, String> mEmail = new HashMap<>();
    @Ignore
    private Map<Integer, String> mPhone = new HashMap<>();
    @Ignore
    private Map<Integer, String> mAddress = new HashMap<>();
    @ColumnInfo(name = "photo_uri")
    public String mPhotoUri;
    @Ignore
    private Set<Long> mGroupIds = new HashSet<>();

    @Ignore
    private char mContactLetterBadge;
    @Ignore
    private char mContactLetterScroll;
    @Ignore
    private Integer mContactColor;

    public ContactImpl() {

    }

    protected ContactImpl(long id, String lookupKey, String displayName, String firstName, String lastName, Uri photoUri) {
        super(id, displayName);

        mLookupKey = lookupKey;
        mFirstName = Helper.isNullOrEmpty(firstName) ? "---" : firstName;
        mLastName = Helper.isNullOrEmpty(lastName) ? "---" : lastName;
        setPhotoUri(photoUri);
    }

    @Override
    public String getFirstName() {
        return mFirstName;
    }

    @Override
    public String getLastName() {
        return mLastName;
    }

    @Override
    public String getEmail(int type) {
        String email = mEmail.get(type);
        if (email == null && !mEmail.isEmpty()) {
            email = mEmail.values().iterator().next();
        }
        return email;
    }

    @Override
    public String getPhone(int type) {
        String phone = mPhone.get(type);
        if (phone == null && !mPhone.isEmpty()) {
            phone = mPhone.values().iterator().next();
        }
        return phone;
    }

    @Override
    public String getAddress(int type) {
        String address = mAddress.get(type);
        if (address == null && !mAddress.isEmpty()) {
            address = mAddress.values().iterator().next();
        }
        return address;
    }

    @Override
    public List<String> getPhone() {
        List<String> phones = new ArrayList<>();
        for(Integer key : mPhone.keySet()) {
            String phone = mPhone.get(key);

            if(phone != null && !phone.isEmpty()) {
                phones.add(phone);
            }
        }

        return phones;
    }

    @Override
    public Map<Integer, String> getPhoneMap() {
        return mPhone;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    @Override
    public char getContactLetter() {
        if (mContactLetterBadge == 0) {
            Matcher m = CONTACT_LETTER.matcher(getDisplayName());
            String letter = m.matches() ? m.group(1).toUpperCase(Locale.US) : "?";
            mContactLetterBadge = Helper.isNullOrEmpty(letter) ? '?' : letter.charAt(0);
        }

        return mContactLetterBadge;
    }

    @Override
    public char getContactLetter(ContactSortOrder sortOrder) {
        if (mContactLetterScroll == 0) {
            String name;
            switch (sortOrder) {
                case FIRST_NAME: name = getFirstName(); break;
                case LAST_NAME: name = getLastName(); break;
                default: name = getDisplayName(); break;
            }
            mContactLetterScroll = Helper.isNullOrEmpty(name) ? '?' :
                    name.toUpperCase(Locale.getDefault()).charAt(0);
        }

        return mContactLetterScroll;
    }

    @Override
    public int getContactColor() {
        if (mContactColor == null) {
            String key = getDisplayName();
            int value = Helper.isNullOrEmpty(key) ? hashCode() : key.hashCode();
            mContactColor = CONTACT_COLORS_MATERIAL[Math.abs(value) % CONTACT_COLORS_MATERIAL.length];
        }
        return mContactColor;
    }

    /**
     * Matches:
     * https://developer.android.com/reference/android/provider/ContactsContract.ContactsColumns.html#LOOKUP_KEY
     *
     * Used as unique key to cache contact pictures for a specific contact and also to create the
     * contact Uri: ContactsContract.Contacts.CONTENT_LOOKUP_URI + "/" + LOOKUP_KEY
     */
    @Override
    public String getLookupKey() {
        return mLookupKey;
    }

    @Override
    public Uri getPhotoUri() {
        return mPhotoUri != null ? Uri.parse(mPhotoUri) : null;
    }

    @Override
    public Set<Long> getGroupIds() {
        return mGroupIds;
    }

    public void setFirstName(String value) {
        mFirstName = value;
    }

    public void setLastName(String value) {
        mLastName = value;
    }

    public void setEmail(int type, String value) {
        mEmail.put(type, value);
    }

    public void setPhone(int type, String value) {
        mPhone.put(type, value);
    }

    public void setAddress(int type, String value) {
        mAddress.put(type, value);
    }

    protected void setPhotoUri(Uri photoUri) {
        mPhotoUri = photoUri != null ? photoUri.toString() : null;
    }

    public void addGroupId(long value) {
        mGroupIds.add(value);
    }

    public void setPhoneNumber(String number) {
        mPhoneNumber = number;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getmPhotoUri() {
        return mPhotoUri;
    }
}
