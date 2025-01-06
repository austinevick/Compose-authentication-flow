package com.austinevick.blockrollclone.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.austinevick.blockrollclone.R
import com.austinevick.blockrollclone.databinding.FragmentHomeBinding
import com.austinevick.blockrollclone.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            composeView.setContent {
                MaterialTheme {

                    val state = viewModel.counterState.collectAsState()
                    val dailyLimit = 50000f
                    val amount = remember { mutableStateOf("") }

                    fun getRemainingLimit(): Float {
                        return if (amount.value.isEmpty()) 0f else
                            dailyLimit - amount.value.toFloat()
                    }

                    fun getProgress(): Float {
                        return if (amount.value.isEmpty()) 0f else
                            amount.value.toFloat() / dailyLimit
                    }

                    Scaffold { innerPadding ->


                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Log.d("Percent", getProgress().toString())

                            LinearProgressIndicator(progress = ::getProgress, color = Color.Red)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Remaining limit: ${getRemainingLimit()}",
                                fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = amount.value, onValueChange = { amount.value = it },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(
                                    fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Daily limit: $dailyLimit",
                                fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                            )

                        }

                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
