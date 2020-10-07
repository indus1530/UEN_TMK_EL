package edu.aku.hassannaqvi.uen_tmk_el.contracts;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FollowUpContract {

    private static final String TAG = "FollowUp_CONTRACT";

    public static String CONTENT_AUTHORITY = "edu.aku.hassannaqvi.uen_tmk_el";


    public static abstract class TableFollowUp implements BaseColumns {

        public static final String TABLE_NAME = "folowupslist";

        public static final String _ID = "id";
        public static final String COLUMN_MP101 = "mp101";
        public static final String COLUMN__LUID = "_luid";
        public static final String COLUMN_MPSYSDATE = "mpsysdate";
        public static final String COLUMN_PID = "pid";
        public static final String COLUMN_SEEM_VID = "seem_vid";

        public static final String SERVER_URI = "followup.php";

        public static String PATH = "folowupslist";

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)
                .buildUpon().appendPath(PATH).build();

        public static String getMovieKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}