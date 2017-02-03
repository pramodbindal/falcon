package com.pramodbindal.localshop;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.pramodbindal.localshop.constants.ApiConstants;
import com.pramodbindal.localshop.db.DBHelper;
import com.pramodbindal.localshop.domain.Customer;
import com.pramodbindal.localshop.domain.Tenant;
import com.pramodbindal.localshop.util.CommonConstants;

import java.util.List;

public class DataSyncService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DataSyncService() {
        super(CommonConstants.APP_NAME);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(CommonConstants.TAG, "Uploading  Data to Server");
        DBHelper dbHelper = new DBHelper(this);
        String deviceId = dbHelper.readAttribute("DEVICE_ID");
        Tenant tenant = new Tenant(null, deviceId, "9876543210", "Alpha User");
        tenant = RestUtils.executePost(ApiConstants.API_URL_TENANT_REGISTER, tenant, Tenant.class);
        if (tenant != null) {
            String tenantId = String.valueOf(tenant.getId());
            dbHelper.saveAttribute("TENANT_ID", tenantId);
            List<Customer> customers = dbHelper.getAllCustomers();
            for (Customer customer : customers) {
                RestUtils.executePost(ApiConstants.API_URL_CUSTOMER, customer, Customer.class, tenantId);
            }
        }

        dbHelper.close();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(CommonConstants.TAG, "Starting Service");
        DBHelper dbHelper = new DBHelper(this);
        String id = InstanceID.getInstance(this).getId();
        dbHelper.saveAttribute("DEVICE_ID", id);
        dbHelper.close();
        return super.onBind(intent);
    }


}
