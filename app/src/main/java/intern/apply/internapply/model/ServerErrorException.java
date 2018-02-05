package intern.apply.internapply.model;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Unknown on 2018-02-05.
 */

public class ServerErrorException extends Throwable {

    private List<ServerError> errs;

    public ServerErrorException() {
        errs = new LinkedList<ServerError>();
    }

    public List<ServerError> getErrors() {
        return errs;
    }

    public void addErrorCode(int code) {
        errs.add(new ServerError(code));
    }
}
