package vn.edu.hcmut.emrre.core.feature.wiki;

import info.bliki.api.Connector;
import info.bliki.api.Link;
import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.api.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.mapping.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikiQuery.class);
    private static User user;
    static {
        user = new User("", "", "https://en.wikipedia.org/w/api.php");
        user.login();
    }

    public static List<Page> getArticals(String title) {
        List<Page> pages = Collections.emptyList();
        String titles[] = substrings(title);
        try {
            Connector connector = new Connector();
            CustomQuery query = new CustomQuery();
            query.titles(titles).redirects().prop("links|categories").aplimit(500);
            pages = connector.query(user, query);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return pages;
    }

    // public static void main(String[] args) {
    // String pre = "liver enzimes";
    // String pos = "biliary stasis";
    //
    // List<Page> preArticles = getArticals(pre);
    // List<Page> posArticles = getArticals(pos);
    //
    // if (preArticles.size() != 0 || posArticles.size() != 0) {
    // System.out.println("WF1 : 1");
    // } else {
    // System.out.println("WF1 : 0");
    // }
    //
    // List<Link> preLinks = new ArrayList<Link>();
    // List<Link> posLinks = new ArrayList<Link>();
    //
    // for (Page page : preArticles) {
    // for (int i = 0; i < page.sizeOfLinksList(); i++) {
    // preLinks.add(page.getLink(i));
    // }
    // }
    // for (Page page : posArticles) {
    // for (int i = 0; i < page.sizeOfLinksList(); i++) {
    // posLinks.add(page.getLink(i));
    // }
    // }
    //
    // for (Page page : preArticles) {
    // for (Link link : posLinks) {
    // if (page.getTitle().equals(link.getTitle())) {
    // System.out.println(" --- " + page + "\n -- " + link);
    // System.out.println("WF3 : 1");
    // }
    // }
    // }
    //
    // for (Page page : posArticles) {
    // for (Link link : preLinks) {
    // if (page.getTitle().equals(link.getTitle())) {
    // System.out.println(" --- " + page + "\n -- " + link);
    // System.out.println("WF4 : 1");
    // }
    // }
    // }
    //
    // for (Link linkPre : preLinks) {
    // for (Link linkPos : posLinks) {
    // if (linkPos.equals(linkPre)) {
    // System.out.println(" --- pre : " + linkPre);
    // System.out.println(" --- pos : " + linkPos);
    // System.out.println("WF5 : 1");
    // }
    // }
    // }
    // }

    public static String[] substrings(String str) {
        String[] nonSenseWordsArr = { "these", "another", "this", "all", "no", "any", "the", "both", "that", "every",
                "some", "each", "neither", "either", "those", "an", "a" };
        for (String string : nonSenseWordsArr) {
            String regex = String.format("\\s%s\\s|\\s%s|%s\\s", string, string, string);
            str = str.toLowerCase().replaceAll(regex, "");
        }
        List<String> result = new ArrayList<String>();
        String[] arr = str.trim().split("\\s+");
        for (int i = 1; i < arr.length + 1; i++) {
            for (int j = 0; j < arr.length + 1 - i; j++) {
                List<String> tmp = new ArrayList<String>();
                for (int k = j; k < j + i; k++) {
                    tmp.add(arr[k]);
                }
                result.add(String.join(" ", tmp));
            }
        }
        return result.stream().toArray(String[]::new);
    }

    public static class CustomQuery extends Query {
        @Override
        public Query aplimit(int limit) {
            put("pllimit", "max");
            return this;
        }
    }
    
    public static void main(String[] args) {
        String str = "ventral hernia";
        List<Page> pages = WikiQuery.getArticals(str);
        for (Page page : pages) {
            System.out.println(page.sizeOfLinksList());
            for (int i = 0; i < 10; i++) {
                System.out.println(page.getLink(i));
            }
        }
        WikiDataWrapper dataWrapper = new WikiDataWrapper(pages);
        System.out.println(dataWrapper.toString());
    }
}