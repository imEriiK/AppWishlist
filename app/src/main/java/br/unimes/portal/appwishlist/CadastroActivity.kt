package br.unimes.portal.appwishlist

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unimes.portal.appwishlist.databinding.ActivityCadastroBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore




class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = Firebase.firestore
        binding.btnCadastrar.setOnClickListener {
            val nome = binding.edtNome.text.toString()
            val preco = binding.edtPreco.text.toString().toDoubleOrNull()
            val lugar = binding.edtLugar.text.toString()
            val item = hashMapOf(
                "nome" to nome,
                "preco" to preco,
                "lugar" to lugar
            )
            db.collection("itens")
                .add(item)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Item cadastrado com sucesso!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Falha ao cadastrar item.", Toast.LENGTH_SHORT).show()
                    }
                }



        }


    }
}
