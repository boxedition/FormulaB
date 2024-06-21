/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.formulab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *  This ViewModel is used to store image classifier helper settings
 */
class MainViewModel : ViewModel() {
    val labelLiveData = MutableLiveData<List<String>>()

    private var _gamesCompleted = 0
    private var _gamesWon = 0

    val gamesCompleted: Int
        get() = _gamesCompleted
    val gamesWon: Int
        get() = _gamesWon

    fun onGameFinished(won: Boolean) {
        _gamesCompleted++
        if (won) {
            _gamesWon++
        }
    }
    private var _delegate: Int = ImageClassifierHelper.DELEGATE_CPU
    private var _threshold: Float =
        ImageClassifierHelper.THRESHOLD_DEFAULT
    private var _maxResults: Int =
        ImageClassifierHelper.MAX_RESULTS_DEFAULT
    private var _model: Int = ImageClassifierHelper.MODEL_EFFICIENTNETV2

    val currentDelegate: Int get() = _delegate
    val currentThreshold: Float get() = _threshold
    val currentMaxResults: Int get() = _maxResults
    val currentModel: Int get() = _model

    fun setDelegate(delegate: Int) {
        _delegate = delegate
    }

    fun setThreshold(threshold: Float) {
        _threshold = threshold
    }

    fun setMaxResults(maxResults: Int) {
        _maxResults = maxResults
    }

    fun setModel(model: Int) {
        _model = model
    }
}
