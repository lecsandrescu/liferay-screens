package com.liferay.mobile.screens.filedisplay;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.liferay.mobile.screens.R;
import com.liferay.mobile.screens.assetdisplay.AssetDisplayListener;
import com.liferay.mobile.screens.assetdisplay.interactor.AssetDisplayInteractorImpl;
import com.liferay.mobile.screens.assetlist.AssetEntry;
import com.liferay.mobile.screens.base.BaseScreenlet;
import com.liferay.mobile.screens.context.SessionContext;

/**
 * @author Sarai Díaz García
 */
public abstract class BaseFileDisplayScreenlet
	extends BaseScreenlet<BaseFileDisplayViewModel, AssetDisplayInteractorImpl> implements AssetDisplayListener {

	public BaseFileDisplayScreenlet(Context context) {
		super(context);
	}

	public BaseFileDisplayScreenlet(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseFileDisplayScreenlet(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BaseFileDisplayScreenlet(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected View createScreenletView(Context context, AttributeSet attributes) {
		TypedArray typedArray =
			context.getTheme().obtainStyledAttributes(attributes, R.styleable.AssetDisplayScreenlet, 0, 0);

		int layoutId = typedArray.getResourceId(R.styleable.AssetDisplayScreenlet_layoutId, getDefaultLayoutId());

		autoLoad = typedArray.getBoolean(R.styleable.AssetDisplayScreenlet_autoLoad, true);
		entryId = typedArray.getInt(R.styleable.AssetDisplayScreenlet_entryId, 0);

		View view = LayoutInflater.from(context).inflate(layoutId, null);

		typedArray.recycle();

		return view;
	}

	@Override
	public void onRetrieveAssetSuccess(AssetEntry assetEntry) {
		fileEntry = (FileEntry) assetEntry;
		getViewModel().showFinishOperation(fileEntry);

		if (listener != null) {
			listener.onRetrieveAssetSuccess(assetEntry);
		}
	}

	@Override
	public void onRetrieveAssetFailure(Exception e) {
		getViewModel().showFailedOperation(null, e);

		if (listener != null) {
			listener.onRetrieveAssetFailure(e);
		}
	}

	@Override
	protected AssetDisplayInteractorImpl createInteractor(String actionName) {
		return new AssetDisplayInteractorImpl(this.getScreenletId());
	}

	@Override
	protected void onScreenletAttached() {
		super.onScreenletAttached();

		if (autoLoad) {
			autoLoad();
		}
	}

	protected void autoLoad() {
		if (SessionContext.isLoggedIn()) {
			try {
				onRetrieveAssetSuccess(fileEntry);
			} catch (Exception e) {
				onRetrieveAssetFailure(e);
			}
		}
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public void setListener(AssetDisplayListener listener) {
		this.listener = listener;
	}

	public void setFileEntry(FileEntry fileEntry) {
		this.fileEntry = fileEntry;
	}

	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	protected boolean autoLoad;
	protected int entryId;
	protected AssetDisplayListener listener;
	protected FileEntry fileEntry;
}
