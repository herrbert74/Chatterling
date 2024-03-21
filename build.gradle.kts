plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.kotlin) apply false
	alias(libs.plugins.ksp)
	alias(libs.plugins.realm) apply false
}

buildscript {
	dependencies {
		classpath(libs.google.dagger.hilt.plugin)
	}

}