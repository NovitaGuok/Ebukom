package com.ebukom.arch.ui.forgotpassword.verification

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.firebase.RegisterParentRequestDao
import com.ebukom.arch.dao.firebase.RegisterSchoolRequestDao
import com.ebukom.arch.ui.forgotpassword.resetpassword.ResetPasswordActivity
import com.ebukom.arch.ui.login.LoginActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_verification.*
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {
    companion object {
        const val LAYOUT_REGISTER = 1
        const val LAYOUT_FORGOT = 0
    }

    private var data: Any? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var storedVerificationId: String = ""
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        initToolbar()

        val layout = intent.getIntExtra("layout", 0)
        data = intent.getSerializableExtra("data")

        val phone = when (data) {
            is RegisterParentRequestDao -> (data as RegisterParentRequestDao).phone
            is RegisterSchoolRequestDao -> (data as RegisterSchoolRequestDao).phone
            else -> null
        }

        auth = FirebaseAuth.getInstance() // Firebase authentication
        db = FirebaseFirestore.getInstance() // Firestore

        if (phone != null) {
            onVerifyPhone(phone)
        }

        when (layout) {
            0 -> {
                // for forgot password
                btnVerificationReset.setOnClickListener {
                    loading.visibility = View.VISIBLE
                    if (txt_pin_entry.text.toString() == "0000") {
                        Handler().postDelayed({
                            loading.visibility = View.GONE
                            startActivity(Intent(this, ResetPasswordActivity::class.java))
                        }, 1000)
                    } else {
                        Handler().postDelayed({
                            loading.visibility = View.GONE
                            tvVerificationErrorMessage.visibility = View.VISIBLE
                        }, 1000)
                    }
                }
            }
            else -> {
                // for register
                tvToolbarTitle.text = "Verifikasi Akun"
                tvVerificationText.text =
                    "Sebelum akun Anda bisa digunakan, Anda harus verifikasi terlebih dahulu. Kami sudah mengirimkan kode ke nomor telepon Anda melalui SMS. Silakan memasukkan 4 digit kode tersebut"
                btnVerificationReset.setText("VERIFIKASI AKUN")

                btnVerificationReset.setOnClickListener {
                    val credential = PhoneAuthProvider.getCredential(
                        storedVerificationId,
                        txt_pin_entry.text.toString()
                    )
                    doRegisterWithPhone(credential)

/*
                    Handler().postDelayed({
                        loading.visibility = View.GONE

                        if (txt_pin_entry.text.toString() == "0000") {
                            Handler().postDelayed({
                                loading.visibility = View.GONE

                                val builder = AlertDialog.Builder(this@VerificationActivity)
                                builder.setMessage("Akun Anda berhasil diverifikasi. Selamat menggunakan.")
                                builder.setPositiveButton("OK") { dialog, which ->
                                    Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
                                }

                                val dialog: AlertDialog = builder.create()
                                dialog.show()

                                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                positiveButton.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.colorSuperDarkBlue
                                    )
                                )

                                positiveButton.setOnClickListener{
                                    startActivity(Intent(this, LoginActivity::class.java))
                                }

                            }, 1000)
                        } else {
                            Handler().postDelayed({
                                loading.visibility = View.GONE
                                tvVerificationErrorMessage.visibility = View.VISIBLE
                            }, 1000)
                        }
                    }, 1000)
                    */
                }
            }
        }

        tvVerificationNotReceiveCode.setOnClickListener {
            txt_pin_entry.setText("")
            phone?.let { it1 -> onVerifyPhone(it1) }
        }

        txt_pin_entry.setOnClickListener {
            tvVerificationCodeReceived.visibility = View.GONE
            tvVerificationErrorMessage.visibility = View.GONE
        }
    }

    private fun onVerifyPhone(phone: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("VerificationActivity", "onVerificationCompleted:$credential")
                val code: String? = credential.getSmsCode()
                if (code != null) {
                    txt_pin_entry.setText(code)
                }

                doRegisterWithPhone(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("VerificationActivity", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(
                        this@VerificationActivity,
                        "Nomor Tidak Valid",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(
                        this@VerificationActivity,
                        "Maaf Kuota SMS Penuh",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("VerificationActivity", "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }


        auth.setLanguageCode("id")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun doRegisterWithPhone(credential: PhoneAuthCredential) {
        loading.visibility = View.VISIBLE

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("VerificationActivity", "signInWithCredential:success")
                    doRegisterWithPassword(credential)
                } else {
                    loading.visibility = View.GONE
                    Log.w("VerificationActivity", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Kode yang dimasukkan tidak valid", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    fun doRegisterWithPassword(phoneCredential: PhoneAuthCredential) {
        // Reformat phone to email like 08xx@phone.id
        var phoneMail = when (data) {
            is RegisterParentRequestDao -> (data as RegisterParentRequestDao).phone.replaceRange(
                0,
                3,
                "0"
            ) + "@phone.id"
            is RegisterSchoolRequestDao -> (data as RegisterSchoolRequestDao).phone.replaceRange(
                0,
                3,
                "0"
            ) + "@phone.id"
            else -> null
        }
        var password = when (data) {
            is RegisterParentRequestDao -> (data as RegisterParentRequestDao).pass
            is RegisterSchoolRequestDao -> (data as RegisterSchoolRequestDao).pass
            else -> null
        }

        if (!phoneMail.isNullOrEmpty() && !password.isNullOrEmpty()) {
            auth.createUserWithEmailAndPassword(
                phoneMail,
                password
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    task.result?.user?.let { it -> insertData(it, data!!) }

/*
                    val credential = PhoneAuthProvider.getCredential(
                        storedVerificationId,
                        txt_pin_entry.text.toString()
                    )

                    auth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            Log.d("VerificationActivity", "linkWithCredential:success")
                            task.result?.user?.let { it -> insertData(it, data!!) }
                        } else {
                            loading.visibility = View.GONE
                            Log.w(
                                "VerificationActivity",
                                "linkWithCredential:failure",
                                task.exception
                            )
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
*/
                } else {
                    loading.visibility = View.GONE
                    Log.e("VerificationActivity", task.exception?.message)
                    Toast.makeText(
                        this,
                        "Registration failed, please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    /**
     * Insert data to Firestore
     */
    private fun insertData(user: FirebaseUser, data: Any) {
        var level = when (data) {
            is RegisterParentRequestDao -> (data as RegisterParentRequestDao).level
            is RegisterSchoolRequestDao -> (data as RegisterSchoolRequestDao).level
            else -> 0
        }

        db.collection("users").document(user.uid).set(data).addOnSuccessListener {
            Toast.makeText(this, "Registration success", Toast.LENGTH_LONG)
                .show()

            loading.visibility = View.GONE
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("role", level)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            loading.visibility = View.GONE
            Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG)
                .show()
        }
    }


    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}