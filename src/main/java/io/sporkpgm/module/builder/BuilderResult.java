package io.sporkpgm.module.builder;

import com.google.common.collect.Lists;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.util.Log;

import java.util.ArrayList;
import java.util.List;

public enum BuilderResult {

	SINGLE {

		@Override
		public List<Module> result(Builder builder, BuilderContext context) {
			return Lists.newArrayList(builder.single(context));
		}

	},
	LIST {

		@Override
		public List<Module> result(Builder builder, BuilderContext context) {
			return Lists.newArrayList(builder.list(context));
		}

	};

	public List<Module> result(Builder builder, BuilderContext context) {
		return new ArrayList<>();
	}

	public static List<Module> build(Class<? extends Module> module, BuilderContext context) {
		if(!module.isAnnotationPresent(ModuleInfo.class)) {
			Log.warning("No ModuleInfo present for " + module.getSimpleName() + " ignoring it");
			return new ArrayList<>();
		}

		ModuleInfo moduleInfo = module.getAnnotation(ModuleInfo.class);
		Class<? extends Builder> builderClass = moduleInfo.builder();
		if(builderClass == null) {
			Log.warning("Invalid Builder present for " + module.getSimpleName() + " ignoring it");
			return new ArrayList<>();
		}

		if(!module.isAnnotationPresent(BuilderInfo.class)) {
			Log.warning("No BuilderInfo present for " + module.getSimpleName() + " ignoring it");
			return new ArrayList<>();
		}

		BuilderInfo builderInfo = module.getAnnotation(BuilderInfo.class);
		Builder builder = BuilderFactory.get(module);

		return builderInfo.result().result(builder, context);
	}

}