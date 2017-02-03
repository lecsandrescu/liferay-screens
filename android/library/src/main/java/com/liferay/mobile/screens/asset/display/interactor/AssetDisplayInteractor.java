package com.liferay.mobile.screens.asset.display.interactor;

import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.screens.asset.AssetEntry;
import com.liferay.mobile.screens.asset.AssetEvent;
import com.liferay.mobile.screens.asset.AssetFactory;
import com.liferay.mobile.screens.asset.display.AssetDisplayListener;
import com.liferay.mobile.screens.asset.display.AssetDisplayScreenlet;
import com.liferay.mobile.screens.asset.list.connector.ScreensAssetEntryConnector;
import com.liferay.mobile.screens.base.interactor.BaseCacheReadInteractor;
import com.liferay.mobile.screens.context.LiferayServerContext;
import com.liferay.mobile.screens.context.SessionContext;
import com.liferay.mobile.screens.service.v70.ScreensassetentryService;
import com.liferay.mobile.screens.util.JSONUtil;
import com.liferay.mobile.screens.util.ServiceProvider;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Sarai Díaz García
 */
public class AssetDisplayInteractor extends BaseCacheReadInteractor<AssetDisplayListener, AssetEvent> {

	@Override
	public AssetEvent execute(Object... args) throws Exception {

		JSONObject jsonObject = getAsset(args);
		return new AssetEvent(jsonObject);
	}

	private JSONObject getAsset(Object... args) throws Exception {
		Session session = SessionContext.createSessionFromCurrentSession();
		if (args.length > 1) {
			String className = (String) args[0];
			long classPK = (long) args[1];

			ScreensassetentryService service = new ScreensassetentryService(session);
			return service.getAssetEntry(className, classPK, Locale.getDefault().getLanguage());
		} else if (args[0] instanceof Long) {
			long entryId = (long) args[0];

			ScreensassetentryService service = new ScreensassetentryService(getSession());
			return service.getAssetEntry(entryId, Locale.getDefault().getLanguage());
		} else {
			String portletItemName = (String) args[0];

			ScreensAssetEntryConnector connector =
				ServiceProvider.getInstance().getScreensAssetEntryConnector(getSession());
			JSONArray assetEntry =
				connector.getAssetEntries(LiferayServerContext.getCompanyId(), groupId, portletItemName,
					locale.toString(), 1);

			if (assetEntry.length() == 0) {
				throw new NoSuchElementException();
			}

			return assetEntry.getJSONObject(0);
		}
	}

	@Override
	public void onSuccess(AssetEvent event) {
		Map<String, Object> map;

		try {
			map = JSONUtil.toMap(event.getJSONObject());
		} catch (JSONException ex) {
			event.setException(ex);
			onFailure(event);
			return;
		}

		AssetEntry assetEntry = AssetFactory.createInstance(map);
		getListener().onRetrieveAssetSuccess(assetEntry);
	}

	@Override
	public void onFailure(AssetEvent event) {
		getListener().error(event.getException(), AssetDisplayScreenlet.DEFAULT_ACTION);
	}

	@Override
	protected String getIdFromArgs(Object... args) {
		final long cacheId;

		if (args.length > 1) {
			cacheId = (long) args[1];
		} else {
			if (args[0] instanceof Long) {
				cacheId = (long) args[0];
			} else {
				return (String) args[0];
			}
		}

		return String.valueOf(cacheId);
	}
}
