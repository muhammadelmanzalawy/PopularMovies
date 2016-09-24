package com.udacity.mohamed.popularmovies.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
		name = "MovieProvider",
		authority = "com.udacity.mohamed.popularmovies",
		database = "movies.db",
		version = 1)
public class MovieProviderConfig implements ckm.simple.sql_provider.annotation.ProviderConfig {
	@Override
	public UpgradeScript[] getUpdateScripts() {
		return new UpgradeScript[0];
	}
}