package com.bookworm.main;


import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_USER;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LOGIN;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_REGISTER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bookworm.common.LoginPlatform;
import com.bookworm.model.User;
import com.bookworm.operation.UserOperation;
import com.bookworm.ws.user.LoginWS;
import com.bookworm.ws.user.RegisterWS;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class MainFragment /*extends Fragment*/ {
	
	private static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add("email");
            add("public_profile");
        }
    };
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	Context context;
	LoginButton faceLoginBtn;
	

	/*
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login, container, false);
	    context=getActivity();
	    
	    faceLoginBtn = (LoginButton) view.findViewById(R.id.facebookLoginButton);
	    faceLoginBtn.setFragment(this);
	    faceLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
	    

	    return view;
	}*/
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    uiHelper = new UiLifecycleHelper(getActivity(), new Session.StatusCallback() {       
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        });
        uiHelper.onCreate(savedInstanceState);

      //  ensureOpenSession();
	    
	}

	 private boolean ensureOpenSession() {
	        if (Session.getActiveSession() == null ||
	                !Session.getActiveSession().isOpened()) {
	            Session.openActiveSession(
	            		context,
	                    this, 
	                    true, 
	                    PERMISSIONS,
	                    new Session.StatusCallback() {
	                        
	                        public void call(Session session, SessionState state, Exception exception) {
	                            onSessionStateChange(session, state, exception);
	                        }
	                    });
	            return false;
	        }
	        return true;
     }*/
	 
	 private boolean sessionHasNecessaryPerms(Session session) {
	        if (session != null && session.getPermissions() != null) {
	            for (String requestedPerm : PERMISSIONS) {
	                if (!session.getPermissions().contains(requestedPerm)) {
	                    return false;
	                }
	            }
	            return true;
	        }
	        return false;
	}
	 
	 private List<String> getMissingPermissions(Session session) {
	        List<String> missingPerms = new ArrayList<String>(PERMISSIONS);
	        if (session != null && session.getPermissions() != null) {
	            for (String requestedPerm : PERMISSIONS) {
	                if (session.getPermissions().contains(requestedPerm)) {
	                    missingPerms.remove(requestedPerm);
	                }
	            }
	        }
	        return missingPerms;
	 }
	

	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	   /* if (state.isOpened() && !sessionHasNecessaryPerms(session)) {
	    	 session.requestNewReadPermissions(
                     new NewPermissionsRequest(
                             MainFragment.this, 
                             getMissingPermissions(session)));   */
		if (state.isOpened()){
	        Log.i(TAG, "Logged in...");
	        makeMeRequest(session);
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	/*
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	*/
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
		new Request.GraphUserCallback() {
		
		public void onCompleted(GraphUser user, Response response) {
		// If the response is successful
		if (session == Session.getActiveSession()) {
		if (user != null) {

		String fullName = user.getName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String fbId = user.getId();
		String username = user.getUsername();
		String email = (String) user.asMap().get("email");
		
		
			try {
				    String loginResult="";
					//böyle bir kullanıcı var ise login ol
					if(new UserOperation().isUserExist(new User(email))){
						
						try {
							String credentials []= {email,fbId};
							//new LoginUserDataTask().execute(credentials).get();
							loginResult=new LoginWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LOGIN,
									credentials[0],credentials[1]).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						
					//kullanıcı mevcut değil ise kaydet ve login ol
					}else{
						User facebookUser = new User(fullName,email,fbId,"",
								                      Integer.valueOf(1),
								                      Integer.valueOf(LoginPlatform.FaceBook.getPlatformNumber()));	
						
						new RegisterWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_REGISTER,
			            		                 facebookUser).get();
			                
			            try {
							String credentials []= {email,fbId};
							//new LoginUserDataTask().execute(credentials).get();
							loginResult=new LoginWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LOGIN,
									credentials[0],credentials[1]).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
			            
					}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		}
		if (response.getError() != null) {
		// Handle errors, will do so later.
		}
		}

		});

		request.executeAsync();
		} 
	
	private void facebookLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }

}