package com.coin2012.wikipulse.karsten.demo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Extractor {

	public String getEditsForCategory(String category) {
		List<Title> titles = getTitlesForCategory(category);
		for (Title title : titles) {
			title.setEdits(this.getEditsForPageId(title.getPageid()));
		}
		Gson gson = this.createConfiguredGson();
		String result = gson.toJson(titles);

		return result;
	}

	private List<Title> getTitlesForCategory(String category) {
		// TODO include pageination
		ClientResource resource = buildQueryForCategoryMembers(category);
		String result = executeQueryToResource(resource);
		List<Title> titles = parseResultToTitles(result);

		return titles;
	}

	private List<Edit> getEditsForPageId(String pageId) {
		// Todo pageinatation possible?
		ClientResource resource = buildQueryForRevisions(pageId);
		String result = executeQueryToResource(resource);
		List<Edit> edits = parseResultToEdits(result, pageId);
		return edits;
	}

	private List<Edit> parseResultToEdits(String result, String pageId) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject().get("pages").getAsJsonObject()
				.get(pageId).getAsJsonObject().getAsJsonArray("revisions");

		Gson gson = this.createConfiguredGson();
		List<Edit> edits = new LinkedList<Edit>();
		for (JsonElement jsonElement : categorymembers) {
			Edit edit = gson.fromJson(jsonElement, Edit.class);
			//edit.setContent(jsonElement.getAsJsonObject().get("*").getAsString());
			edits.add(edit);
		}

		return edits;
	}

	private ClientResource buildQueryForRevisions(String pageId) {
		ClientResource resource = new ClientResource(
				"http://en.wikipedia.org/w/api.php");
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("pageids", pageId);
		resource.getReference().addQueryParameter("prop", "revisions");
		//resource.getReference().addQueryParameter("rvprop", "content|ids|timestamp|flags|comment|user");
		resource.getReference().addQueryParameter("rvprop", "ids|timestamp|flags|comment|user");
		resource.getReference().addQueryParameter("rvlimit", "5");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	private List<Title> parseResultToTitles(String result) {
		JsonParser jsonParser = new JsonParser();
		JsonArray categorymembers = jsonParser.parse(result).getAsJsonObject()
				.get("query").getAsJsonObject()
				.getAsJsonArray("categorymembers");

		Gson gson = this.createConfiguredGson();
		List<Title> titles = new LinkedList<Title>();
		for (JsonElement jsonElement : categorymembers) {
			Title title = gson.fromJson(jsonElement, Title.class);
			titles.add(title);
		}

		return titles;
	}

	private String executeQueryToResource(ClientResource resource) {
		Representation response = resource.get();

		String result = "error";
		try {
			result = response.getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private ClientResource buildQueryForCategoryMembers(String category) {
		ClientResource resource = new ClientResource(
				"http://en.wikipedia.org/w/api.php");
		resource.getReference().addQueryParameter("action", "query");
		resource.getReference().addQueryParameter("list", "categorymembers");
		resource.getReference().addQueryParameter("cmtitle", category);
		resource.getReference().addQueryParameter("cmprop",
				"ids|title|timestamp");
		resource.getReference().addQueryParameter("cmlimit", "500");
		resource.getReference().addQueryParameter("format", "json");
		return resource;
	}

	private Gson createConfiguredGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		return gson;
	}
}
