import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import Home from "./components/home/Home";
import RootLayout from "./components/layouts/RootLayout";
import {
    createBrowserRouter,
    createRoutesFromElements,
    Route,
    RouterProvider,
} from "react-router-dom";

import BookAppointment from "./components/appointment/BookAppointment";
import VeterinarianListing from "./components/veterinarian/VeterinarianListing";
import Veterinarian from "./components/veterinarian/Veterinarian";
import UserRegistration from "./components/user/UserRegistration";
import Login from "./components/auth/Login";
import UserDashboard from "./components/user/UserDashboard";
import UserUpdate from "./components/user/UserUpdate";
import AdminDashboard from "./components/admin/AdminDashboard";
import EmailVerification from "./components/auth/EmailVerification";
import ResetPassword from "./components/auth/ResetPassword";
import PasswordResetRequest from "./components/auth/PasswordResetRequest";
import ProtectedRoute from "./components/auth/ProtectedRoute";
function App() {
    const router = createBrowserRouter(
        createRoutesFromElements(
            <Route path="/" element={<RootLayout></RootLayout>}>
                {/* without auth  */}
                <Route index element={<Home></Home>}></Route>
                <Route
                    path="/doctors"
                    element={<VeterinarianListing></VeterinarianListing>}
                ></Route>
                <Route
                    path="/email-verification"
                    element={<EmailVerification />}
                ></Route>

                <Route path="/login" element={<Login></Login>}></Route>

                <Route
                    path="/register-user"
                    element={<UserRegistration></UserRegistration>}
                ></Route>

                <Route
                    path="/password-rest-request"
                    element={<PasswordResetRequest />}
                />

                <Route
                    path="/veterinarians/:vetId/veterinarian"
                    element={<Veterinarian></Veterinarian>}
                ></Route>

                <Route path="/reset-password" element={<ResetPassword />} />

                {/* with auth  */}

                <Route
                    element={
                        <ProtectedRoute
                            allowedRoles={[
                                "ROLE_PATIENT",
                                "ROLE_ADMIN",
                                "ROLE_VET",
                            ]}
                            useOutlet={true}
                        />
                    }
                >
                    <Route
                        path="/book-appointment/:recipientId/new-appointment"
                        element={<BookAppointment></BookAppointment>}
                    ></Route>

                    <Route
                        path="/user-dashboard/:userId/my-dashboard"
                        element={<UserDashboard></UserDashboard>}
                    ></Route>
                    <Route
                        path="/update-user/:userId/update"
                        element={<UserUpdate></UserUpdate>}
                    ></Route>

                    <Route
                        path="/request-password-reset"
                        element={<PasswordResetRequest />}
                    ></Route>

                    
                </Route>

                {/* admin auth  */}

                <Route
                    element={
                        <ProtectedRoute
                            allowedRoles={["ROLE_ADMIN"]}
                            useOutlet={true}
                        />
                    }
                >
                    <Route
                        path="/admin-dashboard/:userId/admin-dashboard"
                        element={<AdminDashboard />}
                    />
                </Route>
            </Route>
        )
    );
    return (
        <>
            <RouterProvider router={router}></RouterProvider>
        </>
    );
}

export default App;
