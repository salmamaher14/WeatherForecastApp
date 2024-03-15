package com.example.weatherforecastapp.home.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.home.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var hoursRecyclerView: RecyclerView
    private lateinit var houresPerDayAdapter:HoursPerDayListAdapter
    private lateinit var loader_view: ProgressBar
    private lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


}

/*
 private lateinit var allProductsFactory :AllProductsViewModelFactory
    private lateinit var viewModel:AllProductsViewModel
    private lateinit var allProductsRv:RecyclerView
    private lateinit var allProductsAdapter:AllProductsListAdapter
    private lateinit var loader_view: ProgressBar
    private lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_products)
        allProductsFactory= AllProductsViewModelFactory(ProductsRepositoryImpl.getInstance(
            ProductsRemoteDataSourceImpl.getInstance(),ProductLocalDataSourceImpl(this)
        ))

        viewModel=ViewModelProvider(this,allProductsFactory).get(AllProductsViewModel::class.java)

        initUi()

        setUpRecyclerView()

        lifecycleScope.launch {
            viewModel.productState.collect { result ->
                when (result) {
                    is ApiState.Loading -> {
                        loader_view.visibility = View.VISIBLE
                        allProductsRv.visibility = View.GONE
                    }
                    is ApiState.Success -> {
                        loader_view.visibility = View.GONE
                        allProductsRv.visibility = View.VISIBLE
                        allProductsAdapter.submitList(result.data)
                    }
                    is ApiState.Failure -> {
                        loader_view.visibility = View.GONE
                        allProductsRv.visibility = View.GONE
                        Toast.makeText(this@AllProductsActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

//        viewModel.products.observe(
//            this, object :Observer<List<Product>>{
//                override fun onChanged(productsList: List<Product>) {
//                    allProductsAdapter.submitList(productsList)
//                }
//
//            }
//        )



    }
    fun initUi(){
        allProductsRv=findViewById(R.id.allProductsrv)
        loader_view=findViewById(R.id.loader)


    }
    fun  setUpRecyclerView(){
        layoutManager= LinearLayoutManager(this)
        layoutManager.orientation=RecyclerView.HORIZONTAL
        allProductsAdapter= AllProductsListAdapter{
                product: Product ->viewModel.insertProduct(product)
            Toast.makeText(this, "Product added: ${product.title}", Toast.LENGTH_SHORT).show()

        }
        allProductsRv.adapter=allProductsAdapter
        allProductsRv.layoutManager=layoutManager

    }

 */