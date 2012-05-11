/*
 * Copyright (C) 2011 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.*;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;

import com.android.internal.telephony.DataCallState;
import com.android.internal.telephony.gsm.NetworkInfo;
import com.android.internal.telephony.IccCardApplication;
import com.android.internal.telephony.IccCardStatus;

import java.util.ArrayList;


/**
 * Custom SEMC ril
 *
 * {@hide}
 */
public class HisenseRIL extends QualcommNoSimReadyRIL implements CommandsInterface {
    protected String mAid;


    public HisenseRIL(Context context) {
        super(context);
    }

    public HisenseRIL(Context context, int networkMode, int cdmaSubscription) {
        super(context, networkMode, cdmaSubscription);
    }

    @Override
    protected void
    processSolicited (Parcel p) {
        int serial, error;
        boolean found = false;

        serial = p.readInt();
        error = p.readInt();

        RILRequest rr;

        rr = findAndRemoveRequestFromList(serial);

        if (rr == null) {
            Log.w(LOG_TAG, "Unexpected solicited response! sn: "
                            + serial + " error: " + error);
            return;
        }

        Object ret = null;

        if (error == 0 || p.dataAvail() > 0) {
            // either command succeeds or command fails but with data payload
            try {switch (rr.mRequest) {
            /*
 cat libs/telephony/ril_commands.h \
 | egrep "^ *{RIL_" \
 | sed -re 's/\{([^,]+),[^,]+,([^}]+).+/case \1: ret = \2(p); break;/'
             */
            case RIL_REQUEST_GET_SIM_STATUS: ret =  responseIccCardStatus(p); break;
            case RIL_REQUEST_ENTER_SIM_PIN: ret =  responseInts(p); break;
            case RIL_REQUEST_ENTER_SIM_PUK: ret =  responseInts(p); break;
            case RIL_REQUEST_ENTER_SIM_PIN2: ret =  responseInts(p); break;
            case RIL_REQUEST_ENTER_SIM_PUK2: ret =  responseInts(p); break;
            case RIL_REQUEST_CHANGE_SIM_PIN: ret =  responseInts(p); break;
            case RIL_REQUEST_CHANGE_SIM_PIN2: ret =  responseInts(p); break;
            case RIL_REQUEST_ENTER_NETWORK_DEPERSONALIZATION: ret =  responseInts(p); break;
            case RIL_REQUEST_GET_CURRENT_CALLS: ret =  responseCallList(p); break;
            case RIL_REQUEST_DIAL: ret =  responseVoid(p); break;
            case RIL_REQUEST_GET_IMSI: ret =  responseString(p); break;
            case RIL_REQUEST_HANGUP: ret =  responseVoid(p); break;
            case RIL_REQUEST_HANGUP_WAITING_OR_BACKGROUND: ret =  responseVoid(p); break;
            case RIL_REQUEST_HANGUP_FOREGROUND_RESUME_BACKGROUND: ret =  responseVoid(p); break;
            case RIL_REQUEST_SWITCH_WAITING_OR_HOLDING_AND_ACTIVE: ret =  responseVoid(p); break;
            case RIL_REQUEST_CONFERENCE: ret =  responseVoid(p); break;
            case RIL_REQUEST_UDUB: ret =  responseVoid(p); break;
            case RIL_REQUEST_LAST_CALL_FAIL_CAUSE: ret =  responseInts(p); break;
            case RIL_REQUEST_SIGNAL_STRENGTH: ret =  responseSignalStrength(p); break;
            case RIL_REQUEST_REGISTRATION_STATE: ret =  responseStrings(p); break;
            case RIL_REQUEST_GPRS_REGISTRATION_STATE: ret =  responseStrings(p); break;
            case RIL_REQUEST_OPERATOR: ret =  responseStrings(p); break;
            case RIL_REQUEST_RADIO_POWER: ret =  responseVoid(p); break;
            case RIL_REQUEST_DTMF: ret =  responseVoid(p); break;
            case RIL_REQUEST_SEND_SMS: ret =  responseSMS(p); break;
            case RIL_REQUEST_SEND_SMS_EXPECT_MORE: ret =  responseSMS(p); break;
            case RIL_REQUEST_SETUP_DATA_CALL: ret =  responseSetupDataCall(p); break;
            case RIL_REQUEST_SIM_IO: ret =  responseICC_IO(p); break;
            case RIL_REQUEST_SEND_USSD: ret =  responseVoid(p); break;
            case RIL_REQUEST_CANCEL_USSD: ret =  responseVoid(p); break;
            case RIL_REQUEST_GET_CLIR: ret =  responseInts(p); break;
            case RIL_REQUEST_SET_CLIR: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_CALL_FORWARD_STATUS: ret =  responseCallForward(p); break;
            case RIL_REQUEST_SET_CALL_FORWARD: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_CALL_WAITING: ret =  responseInts(p); break;
            case RIL_REQUEST_SET_CALL_WAITING: ret =  responseVoid(p); break;
            case RIL_REQUEST_SMS_ACKNOWLEDGE: ret =  responseVoid(p); break;
            case RIL_REQUEST_GET_IMEI: ret =  responseString(p); break;
            case RIL_REQUEST_GET_IMEISV: ret =  responseString(p); break;
            case RIL_REQUEST_ANSWER: ret =  responseVoid(p); break;
            case RIL_REQUEST_DEACTIVATE_DATA_CALL: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_FACILITY_LOCK: ret =  responseInts(p); break;
            case RIL_REQUEST_SET_FACILITY_LOCK: ret =  responseInts(p); break;
            case RIL_REQUEST_CHANGE_BARRING_PASSWORD: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_NETWORK_SELECTION_MODE: ret =  responseInts(p); break;
            case RIL_REQUEST_SET_NETWORK_SELECTION_AUTOMATIC: ret =  responseVoid(p); break;
            case RIL_REQUEST_SET_NETWORK_SELECTION_MANUAL: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_AVAILABLE_NETWORKS : ret =  responseNetworkInfos(p); break;
            case RIL_REQUEST_DTMF_START: ret =  responseVoid(p); break;
            case RIL_REQUEST_DTMF_STOP: ret =  responseVoid(p); break;
            case RIL_REQUEST_BASEBAND_VERSION: ret =  responseString(p); break;
            case RIL_REQUEST_SEPARATE_CONNECTION: ret =  responseVoid(p); break;
            case RIL_REQUEST_SET_MUTE: ret =  responseVoid(p); break;
            case RIL_REQUEST_GET_MUTE: ret =  responseInts(p); break;
            case RIL_REQUEST_QUERY_CLIP: ret =  responseInts(p); break;
            case RIL_REQUEST_LAST_DATA_CALL_FAIL_CAUSE: ret =  responseInts(p); break;
            case RIL_REQUEST_DATA_CALL_LIST: ret =  responseDataCallList(p); break;
            case RIL_REQUEST_RESET_RADIO: ret =  responseVoid(p); break;
            case RIL_REQUEST_OEM_HOOK_RAW: ret =  responseRaw(p); break;
            case RIL_REQUEST_OEM_HOOK_STRINGS: ret =  responseStrings(p); break;
            case RIL_REQUEST_SCREEN_STATE: ret =  responseVoid(p); break;
            case RIL_REQUEST_SET_SUPP_SVC_NOTIFICATION: ret =  responseVoid(p); break;
            case RIL_REQUEST_WRITE_SMS_TO_SIM: ret =  responseInts(p); break;
            case RIL_REQUEST_DELETE_SMS_ON_SIM: ret =  responseVoid(p); break;
            case RIL_REQUEST_SET_BAND_MODE: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_AVAILABLE_BAND_MODE: ret =  responseInts(p); break;
            case RIL_REQUEST_STK_GET_PROFILE: ret =  responseString(p); break;
            case RIL_REQUEST_STK_SET_PROFILE: ret =  responseVoid(p); break;
            case RIL_REQUEST_STK_SEND_ENVELOPE_COMMAND: ret =  responseString(p); break;
            case RIL_REQUEST_STK_SEND_TERMINAL_RESPONSE: ret =  responseVoid(p); break;
            case RIL_REQUEST_STK_HANDLE_CALL_SETUP_REQUESTED_FROM_SIM: ret =  responseInts(p); break;
            case RIL_REQUEST_EXPLICIT_CALL_TRANSFER: ret =  responseVoid(p); break;
            case RIL_REQUEST_SET_PREFERRED_NETWORK_TYPE: ret =  responseVoid(p); break;
            case RIL_REQUEST_GET_PREFERRED_NETWORK_TYPE: ret =  responseInts(p); break;
            case RIL_REQUEST_GET_NEIGHBORING_CELL_IDS: ret = responseCellList(p); break;
            case RIL_REQUEST_SET_LOCATION_UPDATES: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_SET_SUBSCRIPTION: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_SET_ROAMING_PREFERENCE: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_QUERY_ROAMING_PREFERENCE: ret =  responseInts(p); break;
            case RIL_REQUEST_SET_TTY_MODE: ret =  responseVoid(p); break;
            case RIL_REQUEST_QUERY_TTY_MODE: ret =  responseInts(p); break;
            case RIL_REQUEST_CDMA_SET_PREFERRED_VOICE_PRIVACY_MODE: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_QUERY_PREFERRED_VOICE_PRIVACY_MODE: ret =  responseInts(p); break;
            case RIL_REQUEST_CDMA_FLASH: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_BURST_DTMF: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_SEND_SMS: ret =  responseSMS(p); break;
            case RIL_REQUEST_CDMA_SMS_ACKNOWLEDGE: ret =  responseVoid(p); break;
            case RIL_REQUEST_GSM_GET_BROADCAST_CONFIG: ret =  responseGmsBroadcastConfig(p); break;
            case RIL_REQUEST_GSM_SET_BROADCAST_CONFIG: ret =  responseVoid(p); break;
            case RIL_REQUEST_GSM_BROADCAST_ACTIVATION: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_GET_BROADCAST_CONFIG: ret =  responseCdmaBroadcastConfig(p); break;
            case RIL_REQUEST_CDMA_SET_BROADCAST_CONFIG: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_BROADCAST_ACTIVATION: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_VALIDATE_AND_WRITE_AKEY: ret =  responseVoid(p); break;
            case RIL_REQUEST_CDMA_SUBSCRIPTION: ret =  responseStrings(p); break;
            case RIL_REQUEST_CDMA_WRITE_SMS_TO_RUIM: ret =  responseInts(p); break;
            case RIL_REQUEST_CDMA_DELETE_SMS_ON_RUIM: ret =  responseVoid(p); break;
            case RIL_REQUEST_DEVICE_IDENTITY: ret =  responseStrings(p); break;
            case RIL_REQUEST_GET_SMSC_ADDRESS: ret = responseString(p); break;
            case RIL_REQUEST_SET_SMSC_ADDRESS: ret = responseVoid(p); break;
            case RIL_REQUEST_EXIT_EMERGENCY_CALLBACK_MODE: ret = responseVoid(p); break;
            case RIL_REQUEST_REPORT_SMS_MEMORY_STATUS: ret = responseVoid(p); break;
            case RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING: ret = responseVoid(p); break;
            case 104: ret = responseInts(p); break; // RIL_REQUEST_VOICE_RADIO_TECH
            case 105: ret = responseInts(p); break; // RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE
            case 106: ret = responseStrings(p); break; // RIL_REQUEST_CDMA_PRL_VERSION
            case 107: ret = responseInts(p);  break; // RIL_REQUEST_IMS_REGISTRATION_STATE
            case 108: ret = responseSMS(p);  break; // RIL_REQUEST_IMS_SEND_SMS

            default:
                throw new RuntimeException("Unrecognized solicited response: " + rr.mRequest);
            //break;
            }} catch (Throwable tr) {
                // Exceptions here usually mean invalid RIL responses

                Log.w(LOG_TAG, rr.serialString() + "< "
                        + requestToString(rr.mRequest)
                        + " exception, possible invalid RIL response", tr);

                if (rr.mResult != null) {
                    AsyncResult.forMessage(rr.mResult, null, tr);
                    rr.mResult.sendToTarget();
                }
                rr.release();
                return;
            }
        }

        if (error != 0) {
            rr.onError(error, ret);
            rr.release();
            return;
        }

        if (RILJ_LOGD) riljLog(rr.serialString() + "< " + requestToString(rr.mRequest)
            + " " + retToString(rr.mRequest, ret));

        if (rr.mResult != null) {
            AsyncResult.forMessage(rr.mResult, ret, null);
            rr.mResult.sendToTarget();
        }

        rr.release();
    }

    @Override
    public void
    supplyIccPin(String pin, Message result) {
        //       but this request is also valid for SIM and RUIM
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PIN, result);


        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(2);
        rr.mp.writeString(pin);
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    supplyIccPuk(String puk, String newPin, Message result) {
        //Note: This RIL request has not been renamed to ICC,
        //       but this request is also valid for SIM and RUIM
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PUK, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(3);
        rr.mp.writeString(puk);
        rr.mp.writeString(newPin);
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    supplyIccPin2(String pin, Message result) {
        //Note: This RIL request has not been renamed to ICC,
        //       but this request is also valid for SIM and RUIM
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PIN2, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(2);
        rr.mp.writeString(pin);
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    supplyIccPuk2(String puk, String newPin2, Message result) {
        //Note: This RIL request has not been renamed to ICC,
        //       but this request is also valid for SIM and RUIM
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_SIM_PUK2, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(3);
        rr.mp.writeString(puk);
        rr.mp.writeString(newPin2);
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    changeIccPin(String oldPin, String newPin, Message result) {
        //Note: This RIL request has not been renamed to ICC,
        //       but this request is also valid for SIM and RUIM
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_SIM_PIN, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(3);
        rr.mp.writeString(oldPin);
        rr.mp.writeString(newPin);
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    changeIccPin2(String oldPin2, String newPin2, Message result) {
        //Note: This RIL request has not been renamed to ICC,
        //       but this request is also valid for SIM and RUIM
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_SIM_PIN2, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(3);
        rr.mp.writeString(oldPin2);
        rr.mp.writeString(newPin2);
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    changeBarringPassword(String facility, String oldPwd, String newPwd, Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CHANGE_BARRING_PASSWORD, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(3);
        rr.mp.writeString(facility);
        rr.mp.writeString(oldPwd);
        rr.mp.writeString(newPwd);

        send(rr);
    }

    @Override
    public void
    supplyNetworkDepersonalization(String netpin, Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_ENTER_NETWORK_DEPERSONALIZATION, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mp.writeInt(3);
        rr.mp.writeString(netpin);

        send(rr);
    }

    @Override
    public void
    dial(String address, int clirMode, UUSInfo uusInfo, Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_DIAL, result);

        rr.mp.writeString(address);
        rr.mp.writeInt(clirMode);

        if (uusInfo == null) {
            rr.mp.writeInt(0); // UUS information is absent
        } else {
            rr.mp.writeInt(1); // UUS information is present
            rr.mp.writeInt(uusInfo.getType());
            rr.mp.writeInt(uusInfo.getDcs());
            rr.mp.writeByteArray(uusInfo.getUserData());
        }

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    @Override
    public void
    getIMSI(Message result) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_GET_IMSI, result);
        rr.mp.writeInt(1);
        rr.mp.writeString(mAid);
        if (RILJ_LOGD) riljLog(rr.serialString() +
                              "> getIMSI:RIL_REQUEST_GET_IMSI " +
                              RIL_REQUEST_GET_IMSI +
                              " " + requestToString(rr.mRequest));

        send(rr);
    }

    @Override
    public void
    setupDataCall(String radioTechnology, String profile, String apn,
            String user, String password, String authType, String protocol,
            Message result) {
        RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_SETUP_DATA_CALL, result);

        rr.mp.writeInt(7);

        rr.mp.writeString(radioTechnology);
        rr.mp.writeString(profile);
        rr.mp.writeString(apn);
        rr.mp.writeString(user);
        rr.mp.writeString(password);
        rr.mp.writeString(authType);
        rr.mp.writeString(protocol);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> "
                + requestToString(rr.mRequest) + " " + radioTechnology + " "
                + profile + " " + apn + " " + user + " "
                + password + " " + authType + " " + protocol);

        send(rr);
    }

    @Override
    public void
    iccIO (int command, int fileid, String path, int p1, int p2, int p3,
            String data, String pin2, Message result) {
        //Note: This RIL request has not been renamed to ICC,
        //       but this request is also valid for SIM and RUIM
        RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_SIM_IO, result);

        rr.mp.writeInt(command);
        rr.mp.writeInt(fileid);
        rr.mp.writeString(path);
        rr.mp.writeInt(p1);
        rr.mp.writeInt(p2);
        rr.mp.writeInt(p3);
        rr.mp.writeString(data);
        rr.mp.writeString(pin2);
        rr.mp.writeString(mAid);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> iccIO: " + requestToString(rr.mRequest)
                + " 0x" + Integer.toHexString(command)
                + " 0x" + Integer.toHexString(fileid) + " "
                + " path: " + path + ","
                + p1 + "," + p2 + "," + p3);

        send(rr);
    }

    @Override
    public void
    setNetworkSelectionModeAutomatic(Message response) {
        RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_SET_NETWORK_SELECTION_AUTOMATIC,
                                    response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        send(rr);
    }

    @Override
    public void
    setNetworkSelectionModeManual(String operatorNumeric, Message response) {
        RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_SET_NETWORK_SELECTION_MANUAL,
                                    response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest)
                    + " " + operatorNumeric);

        rr.mp.writeString(operatorNumeric);

        send(rr);
    }

    @Override
    public void
    queryFacilityLock (String facility, String password, int serviceClass,
                            Message response) {
        RILRequest rr = RILRequest.obtain(RIL_REQUEST_QUERY_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        // count strings
        rr.mp.writeInt(4);
        rr.mp.writeString(facility);
        rr.mp.writeString(password);
        rr.mp.writeString(Integer.toString(serviceClass));
        rr.mp.writeString(mAid);

        send(rr);
    }

    @Override
    public void
    setFacilityLock (String facility, boolean lockState, String password,
                        int serviceClass, Message response) {
        String lockString;
         RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_SET_FACILITY_LOCK, response);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        // count strings
        rr.mp.writeInt(5);
        rr.mp.writeString(facility);
        rr.mp.writeString(lockState?"1":"0");
        rr.mp.writeString(password);
        rr.mp.writeString(Integer.toString(serviceClass));
        rr.mp.writeString(mAid);

        send(rr);
    }

    // XXX: xxxxxxxxxxxxxxxxxxxxxxxx
    @Override
    protected Object
    responseIccCardStatus(Parcel p) {
        IccCardStatus status = new IccCardStatus();
        status.setCardState(p.readInt());
        status.setUniversalPinState(p.readInt());

        int subscription3gppAppIndex = p.readInt();
        status.setGsmUmtsSubscriptionAppIndex(subscription3gppAppIndex);
        int subscription3gpp2AppIndex = p.readInt();
        status.setCdmaSubscriptionAppIndex(subscription3gpp2AppIndex);
        int subscriptionImsAppIndex = p.readInt();

        int numApplications = p.readInt();
        // limit to maximum allowed applications
        if (numApplications > IccCardStatus.CARD_MAX_APPS) {
            numApplications = IccCardStatus.CARD_MAX_APPS;
        }
        status.setNumApplications(numApplications);

        for (int i = 0 ; i < numApplications ; i++) {
            IccCardApplication ca = new IccCardApplication();
            ca.app_type       = ca.AppTypeFromRILInt(p.readInt());
            ca.app_state      = ca.AppStateFromRILInt(p.readInt());
            ca.perso_substate = ca.PersoSubstateFromRILInt(p.readInt());
            ca.aid            = p.readString();
            ca.app_label      = p.readString();
            ca.pin1_replaced  = p.readInt();
            ca.pin1           = p.readInt();
            ca.pin2           = p.readInt();
            status.addApplication(ca);

        }
        int appIndex = -1;
        if (mPhoneType == RILConstants.CDMA_PHONE) {
            appIndex = status.getCdmaSubscriptionAppIndex();
            Log.d(LOG_TAG, "This is a CDMA PHONE " + appIndex);
        } else {
            appIndex = status.getGsmUmtsSubscriptionAppIndex();
            Log.d(LOG_TAG, "This is a GSM PHONE " + appIndex);
        }

        IccCardApplication application = status.getApplication(appIndex);
        mAid = application.aid;

        return status;
    }
    
    protected Object
    responseSetupDataCall(Parcel p) {
        if (p.readInt() != 6)
            throw new RuntimeException("Expecting Version 6 RIL compliant response.");

        int num = p.readInt();
        ArrayList<DataCallState> response = new ArrayList<DataCallState>(num);;

        int status = 0;
        int cid = 0;
        int active = 0;
        String type = "";
        String ifname = "";
        String address = "";
        String dnses = "";
        String gateways = "";

        for (int i = 0; i < num; i++) {
            DataCallState dataCall = new DataCallState();
            status = p.readInt();
            cid = p.readInt();
            active = p.readInt();
            type = p.readString();
            ifname = p.readString();
            address = p.readString();
            dnses = p.readString();
            gateways = p.readString();
            response.add(dataCall);
        }

        return new String[] { Integer.toString(cid), ifname, address };
    }

    @Override
    protected Object
    responseDataCallList(Parcel p) {
        if (p.readInt() != 6)
            throw new RuntimeException("Expecting Version 6 RIL compliant response.");

        int num = p.readInt();
        ArrayList<DataCallState> response = new ArrayList<DataCallState>(num);;

        for (int i = 0; i < num; i++) {
            DataCallState dataCall = new DataCallState();
            /*dataCall.status =*/ p.readInt();
            dataCall.cid = p.readInt();
            dataCall.active = p.readInt();
            dataCall.type = p.readString();
            /*dataCall.ifname =*/ p.readString();
            dataCall.address = p.readString();
            /*dataCall.dnses =*/ p.readString();
            /*dataCall.gateways =*/ p.readString();
            response.add(dataCall);
        }

        return response;
    }

    @Override
    protected Object
    responseNetworkInfos(Parcel p) {
        String strings[] = (String [])responseStrings(p);
        ArrayList<NetworkInfo> ret;

        if (strings.length % 4 != 0) {
            throw new RuntimeException(
                "RIL_REQUEST_QUERY_AVAILABLE_NETWORKS: invalid response. Got "
                + strings.length + " strings, expected multible of 4");
        }

        ret = new ArrayList<NetworkInfo>(strings.length / 4);

        for (int i = 0 ; i < strings.length ; i += 4) {
            ret.add (
                new NetworkInfo(
                    strings[i+0],
                    strings[i+1],
                    strings[i+2],
                    strings[i+3]));
        }

        return ret;
    }

    @Override
    public void writeSmsToRuim(int status, String pdu, Message response) {
        status = translateStatus(status);

        RILRequest rr = RILRequest.obtain(RIL_REQUEST_CDMA_WRITE_SMS_TO_RUIM,
                response);

        rr.mp.writeInt(status);
        rr.mp.writeString(pdu);

        if (RILJ_LOGD) riljLog("XXXXXXXXXXXXXXXXXX writeSmsToRuim: " + rr.serialString() + "> "
                + requestToString(rr.mRequest)
                + " " + status);

        send(rr);
    }

    @Override
    public void
    deactivateDataCall(int cid, Message result) {
        RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_DEACTIVATE_DATA_CALL, result);

        rr.mp.writeInt(2);
        rr.mp.writeString(Integer.toString(cid));
        rr.mp.writeString(Integer.toString(0));

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " +
                requestToString(rr.mRequest) + " " + cid);

        send(rr);
    }

    @Override
    protected Object
    responseSignalStrength(Parcel p) {
        int numInts = 12;
        int response[];

        /* TODO: Add SignalStrength class to match RIL_SignalStrength */
        response = new int[numInts];
        for (int i = 0 ; i < numInts ; i++) {
            response[i] = p.readInt();
        }

        return response;
    }
}
