package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((requireActivity().application as AsteroidApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.asteroids_list)

        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
                viewModel.openDetailFragment(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidList.observe(viewLifecycleOwner) { asteroids ->
            if (asteroids != null) {
                adapter.submitList(asteroids)
            } else {
                println("asteroidList is null")
            }
        }

        viewModel.navigateToDetailFragmentEvent.observe(viewLifecycleOwner) { asteroid ->
            if (asteroid != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.doneNavigating()
            } else {
                println("asteroid is null")
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_all_menu -> viewModel.onViewWeekAsteroidsMenuItemSelected()
            R.id.show_today_menu -> viewModel.onTodayAsteroidsMenuItemSelected()
            R.id.show_save_menu -> viewModel.onSavedAsteroidsMenuItemSelected()
        }
        return true
    }

}
