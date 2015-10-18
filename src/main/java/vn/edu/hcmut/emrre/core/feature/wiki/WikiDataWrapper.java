package vn.edu.hcmut.emrre.core.feature.wiki;

import info.bliki.api.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class WikiDataWrapper implements Serializable {
    private List<String> titles;
    private List<String> links;
    private List<String> categories;

    public WikiDataWrapper() {
        titles = new ArrayList<String>();
        links = new ArrayList<String>();
        categories = new ArrayList<String>();
    }

    public WikiDataWrapper(List<Page> pages) {
        this();
        for (Page page : pages) {
            if (!page.isMissing()) {
                titles.add(page.getTitle());
            }
            for (int i = 0; i < page.sizeOfLinksList(); i++) {
                links.add(page.getLink(i).getTitle());

            }
            for (int i = 0; i < page.sizeOfCategoryList(); i++) {
                categories.add(page.getCategory(i).getTitle());
            }
        }
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return String.format("Titles : \n %s \n Links:\n %s \n Categories:\n%s\n", titles.toString(), links.toString(),
                categories.toString());
    }

}
