/**
 * 
 */
package controllers;

import com.google.inject.Inject;

import dao.TweedleRequestDao;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author abhishek
 *
 */
public class TweedleController extends Controller {

    @Inject TweedleRequestDao dao;
    public Result getTweedles(String userId) {
        return ok();
    }

}
