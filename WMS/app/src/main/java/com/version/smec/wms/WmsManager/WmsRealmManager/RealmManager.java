package com.version.smec.wms.WmsManager.WmsRealmManager;

import android.content.Context;

import org.json.JSONArray;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by xupeizuo on 2017/2/21.
 */

public final class RealmManager {

    private static Realm realm;
    private static volatile RealmManager realmManager;

    private static final String REALM_NAME="smecWMS";
    private static final int REALM_VERSION=0;
    private static byte[] syncObj = new byte[0];

    public static RealmManager getInstance(){
        if(realmManager == null){
            synchronized (syncObj){
                if(realmManager == null){
                    realmManager=new RealmManager();
                    realm=Realm.getDefaultInstance();
                }
            }
        }
        return realmManager;
    }

    public static void initRealm(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(REALM_NAME).schemaVersion(REALM_VERSION).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public Realm getRealm(){
        return realm;
    }


    /*********************************************分割线****************************************************/


    /**
     * 获取事务
     */
    private void _getTransaction(){
        if(realm.isInTransaction()){
            return;
        }else {
            realm.beginTransaction();
        }
    }



    /**
     * 添加(性能优于下面的saveOrUpdate（）方法)
     *
     * @param object
     * @return 保存或者修改是否成功
     */
    public boolean insert(RealmObject object) {
        try {

            _getTransaction();
            realm.insert(object);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加(性能优于下面的saveOrUpdateBatch（）方法)
     *
     * @param list
     * @return 批量保存是否成功
     */
    public boolean insert(List<? extends RealmObject> list) {
        try {
            _getTransaction();
            realm.insert(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加或者修改(性能优于下面的saveOrUpdate（）方法)
     *
     * @param object
     * @return 保存或者修改是否成功
     */
    public boolean insertOrUpdate(RealmObject object) {
        try {
            _getTransaction();
            realm.insertOrUpdate(object);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加或者修改(性能优于下面的saveOrUpdateBatch（）方法)
     *
     * @param list
     * @return 保存或者修改是否成功
     */
    public boolean insertOrUpdateBatch(List<? extends RealmObject> list) {
        try {
            _getTransaction();
            realm.insertOrUpdate(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加或者修改
     *
     * @param object
     * @return 已经保存的对象
     */
    public <E extends RealmObject> E saveOrUpdate(E object) {
        E savedE = null;
        try {
            _getTransaction();
            savedE = realm.copyToRealmOrUpdate(object);
            realm.commitTransaction();
            return savedE;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return savedE;
        }
    }

    /**
     * 批量添加或者修改
     *
     * @param list
     * @return 全部保存成功 或 全部失败
     */
    public boolean saveOrUpdateBatch(List<? extends RealmObject> list) {
        try {
            _getTransaction();
            realm.copyToRealmOrUpdate(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 批量添加或者修改
     *
     * @param list
     * @return 全部保存成功 或 全部失败
     */
    public boolean saveOrUpdateRealmModelBatch(List list) {
        try {
            _getTransaction();
            realm.copyToRealmOrUpdate(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }


    /**
     * save or update RealmObject from json data
     *
     * @param jsonObject json数据
     * @param clazz      具体类型
     * @return 已经保存的对象
     */
    public RealmObject saveOrUpdateFromJSON(Class<? extends RealmObject> clazz, String jsonObject) {
        RealmObject RealmObject = null;
        try {
            _getTransaction();
            RealmObject = realm.createOrUpdateObjectFromJson(clazz, jsonObject);
            realm.commitTransaction();
            return RealmObject;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return RealmObject;
        }
    }

    /**
     * batch save or update from json array
     *
     * @param json  json数组
     * @param clazz 类型
     * @return 批量保存json对象是否成功
     */
    public boolean saveOrUpdateFromJSONBatch(Class<? extends RealmObject> clazz, JSONArray json) {
        try {
            _getTransaction();
            realm.createOrUpdateAllFromJson(clazz, json);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 删除当前表中所有数据
     *
     * @param clazz
     * @return
     */
    public boolean deleteAll(Class<? extends RealmObject> clazz) {
        try {
            _getTransaction();
            realm.delete(clazz);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 按照id删除制定的记录
     *
     * @param clazz       类型
     * @param idFieldName id字段的名称
     * @param id          id字段值
     * @return
     */
    public boolean deleteById(Class<? extends RealmObject> clazz, String idFieldName, int id) {
        try {
            _getTransaction();
            realm.where(clazz).equalTo(idFieldName, id).findAll().deleteFirstFromRealm();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 查询所有
     *
     * @return 返回结果集合
     */
    public RealmResults<? extends RealmObject> findAll(Class<? extends RealmObject> clazz) {
        return realm.where(clazz).findAll();
    }

    /**
     * 查询符合条件的所有结果
     *
     * @return 返回结果集合
     */
    public  RealmResults<? extends RealmObject> findAllByKey(Class<? extends RealmObject> clazz, String key, String value) {
        return realm.where(clazz).equalTo(key,value).findAll();
    }

    public  RealmResults<? extends RealmObject> findAllByKey(Class<? extends RealmObject> clazz, String key, int value) {
        return realm.where(clazz).equalTo(key,value).findAll();
    }

    public  RealmResults<? extends RealmObject> findAllByKey(Class<? extends RealmObject> clazz, String key1, int value1, String key2, int value2) {
        return realm.where(clazz).equalTo(key1,value1).equalTo(key2,value2).findAll();
    }

    public  RealmResults<? extends RealmObject> findAllByKey(Class<? extends RealmObject> clazz, String key1, int value1, String key2, String value2) {
        return realm.where(clazz).equalTo(key1,value1).equalTo(key2,value2).findAll();
    }

    public  RealmResults<? extends RealmObject> findAllByKey(Class<? extends RealmObject> clazz, String key1, int value1, String key2, int value2, String key3, String value3) {
        return realm.where(clazz).equalTo(key1,value1).equalTo(key2,value2).equalTo(key3,value3).findAll();
    }



    /**
     * 查询唯一结果
     * @param key
     * @param flag
     * @param
     * @return
     */
    public RealmObject findFirst(Class<? extends RealmObject> clazz, String key, boolean flag){
        return realm.where(clazz).equalTo(key,flag).findFirst();

    }

    /**
     * 查询唯一结果
     * @param key
     * @param value
     * @param
     * @return
     */
    public RealmObject findFirst(Class<? extends RealmObject> clazz, String key, String value){
        return realm.where(clazz).equalTo(key,value).findFirst();

    }

    /**
     * 查询结果
     * @param key
     * @param flag
     * @param
     * @return
     */
    public RealmResults findRealmModel(Class clazz, String key, String flag){
        return realm.where(clazz).equalTo(key,flag).findAll();

    }

    /**
     * 删除指RealmObject
     * @param clazz
     * @return
     */
    public boolean deleteRealmObject(Class<? extends RealmObject> clazz){
        try {
            _getTransaction();
            realm.where(clazz).findAll().deleteAllFromRealm();
            realm.commitTransaction();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 按照key删除指定的记录
     * @param clazz
     * @param keyFieldName
     * @param value
     * @return
     */
    public boolean deleteByKey(Class<? extends RealmObject> clazz, String keyFieldName, String value) {
        try {
            _getTransaction();
            realm.where(clazz).equalTo(keyFieldName, value).findAll().deleteAllFromRealm();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 按照key删除指定的记录
     * @param clazz
     * @param keyFieldName
     * @param value
     * @return
     */
    public boolean deleteRealmModelByKey(Class clazz, String keyFieldName, String value) {
        try {
            _getTransaction();
            realm.where(clazz).equalTo(keyFieldName, value).findAll().deleteAllFromRealm();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 清空数据库
     *
     * @return
     */
    public boolean clearDatabase() {
        try {
            _getTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

}
