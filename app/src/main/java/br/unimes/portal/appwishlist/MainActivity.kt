package br.unimes.portal.appwishlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unimes.portal.appwishlist.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()
            auth.signInWithEmailAndPassword(
                email, senha
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, CadastroActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Sucesso!", LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this, "Falha!${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha e-mail e senha para cadastrar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        // TODO: Navegar para a próxima tela ou limpar campos
                    } else {
                        // Mensagem de erro mais clara para o usuário
                        val exception = task.exception?.message ?: "Erro ao cadastrar."
                        Toast.makeText(this, "Falha ao cadastrar: $exception", Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}