package br.unimes.portal.appwishlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unimes.portal.appwishlist.databinding.ActivityCadastroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        binding.btnCadastrar.setOnClickListener {
            cadastrarItem()
        }


        binding.btnFabLista.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cadastrarItem() {
        val nome = binding.edtNome.text.toString()
        val precoText = binding.edtPreco.text.toString()
        val lugar = binding.edtLugar.text.toString()


        if (nome.isBlank()) {
            Toast.makeText(this, "O nome do item é obrigatório.", Toast.LENGTH_SHORT).show()
            return
        }


        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Erro: Usuário não está logado. Faça o login novamente.", Toast.LENGTH_LONG).show()
            return
        }


        val preco = if (precoText.isNotBlank()) precoText.toDoubleOrNull() else null


        val item = hashMapOf(
            "nome" to nome,
            "preco" to preco,
            "lugar" to lugar
        )


        db.collection("usuarios").document(userId).collection("itens")
            .add(item)
            .addOnSuccessListener {
                Toast.makeText(this, "Item cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                limparCampos()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Falha ao cadastrar item: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun limparCampos() {
        binding.edtNome.text.clear()
        binding.edtPreco.text.clear()
        binding.edtLugar.text.clear()
        binding.edtNome.requestFocus()
    }
}

